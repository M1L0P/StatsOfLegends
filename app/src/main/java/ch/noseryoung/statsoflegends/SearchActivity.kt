package ch.noseryoung.statsoflegends

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.noseryoung.statsoflegends.data.DataHolder
import ch.noseryoung.statsoflegends.data.servers
import ch.noseryoung.statsoflegends.net.APIManager
import ch.noseryoung.statsoflegends.net.HTTPManager.loadMapping
import ch.noseryoung.statsoflegends.persistence.RecentSummonerData
import ch.noseryoung.statsoflegends.persistence.RecentSummonerDb
import kotlinx.android.synthetic.main.activity_search.*
import android.widget.AdapterView


class SearchActivity : AppCompatActivity() {

    lateinit var dbInstance: RecentSummonerDb
    lateinit var dbWorkerThread: HandlerThread
    lateinit var dbThreadHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val spinnerArrayAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            servers.keys.toList()
        )

        dbInstance = RecentSummonerDb.getInstance(this)!!
        dbWorkerThread = HandlerThread("dbWorker")
        dbWorkerThread.start()
        dbThreadHandler = Handler(dbWorkerThread.looper)


        spinnerArrayAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        spnSearchServers.setAdapter(spinnerArrayAdapter)

        btnSearchHistory.setOnClickListener {
            startNavigation(NavigationType.HISTORY)

            // Get champion and spell mapping
            loadMapping(this, R.string.url_champmap, R.string.local_champmap)
            loadMapping(this, R.string.url_spellmap, R.string.local_spellmap)
        }

        btnSearchSummary.setOnClickListener {
            startNavigation(NavigationType.SUMMARY)

            // Get champion and spell mapping
            loadMapping(this, R.string.url_champmap, R.string.local_champmap)
            loadMapping(this, R.string.url_spellmap, R.string.local_spellmap)
        }
    }

    override fun onResume() {
        super.onResume()
        setAsyncListViewAdapter()
    }

    fun persistRecentSummoner(summonerName: String, region: String) {
        val summonerToPersist = RecentSummonerData(summonerName = summonerName, region = region)

        val task = Runnable {
            if(dbInstance == null) return@Runnable
            dbInstance.RecentSummonerDao().deleteBySummonerName(summonerName)

            val summonerCount = dbInstance.RecentSummonerDao().getCount()
            if (summonerCount >= 3) {
                dbInstance.RecentSummonerDao().deleteOldest()
            }
            dbInstance.RecentSummonerDao().insert(summonerToPersist)
        }

        dbWorkerThread.looper
        dbThreadHandler.post(task)
    }

    fun setAsyncListViewAdapter() {
        val task = Runnable {
            if(dbInstance == null) return@Runnable
            val summoners = dbInstance.RecentSummonerDao().getAll()

            val nameList = ArrayList<String>()

            for (summoner in summoners) {
                nameList.add(summoner.summonerName)
            }

            if (nameList.size == 0) {
                searchLayout.visibility = View.GONE
            }

            val adapter = ArrayAdapter<String>(this, R.layout.recent_list_entry, nameList.reversed())
            listSearchRecent.adapter = adapter
            adapter.notifyDataSetChanged()

            listSearchRecent.onItemClickListener = object : android.widget.AdapterView.OnItemClickListener {
                override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val item = listSearchRecent.getItemAtPosition(position) as String
                    txtSummonerName.setText(item)
                }
            }
        }

        dbWorkerThread.looper
        dbThreadHandler.post(task)
    }

    fun startNavigation(type: NavigationType) {
        if(txtSummonerName.text == null || txtSummonerName.text!!.isEmpty()) {
            Toast.makeText(this, "No summoner name entered", Toast.LENGTH_SHORT).show()
            return
        }

        val summonerName = txtSummonerName.text.toString()

        if(summonerName != DataHolder.summoner.name) {
            persistRecentSummoner(summonerName, region = "euw1")
            var searchSuceeded = false

            val nameCheckThread = Thread(Runnable {
                searchSuceeded = APIManager.fetch(this, summonerName)
            })

            nameCheckThread.start()
            nameCheckThread.join()

            if (!searchSuceeded) {
                Toast.makeText(this, "Name does not exist on specified server", Toast.LENGTH_LONG).show()
                return
            }
        }

        val intent = Intent(this, NavigationActivity::class.java)
        intent.putExtra("type", type.value)

        startActivity(intent)
    }
}

enum class NavigationType(val value: Int) {
    HISTORY(0),
    SUMMARY(1)
}