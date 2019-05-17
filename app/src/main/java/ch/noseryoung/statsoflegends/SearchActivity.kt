package ch.noseryoung.statsoflegends

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
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

    // lateinit variables for database connection and ThreadHandling
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

        // Initialize database instance and start dbWorker thread
        dbInstance = RecentSummonerDb.getInstance(this)!!
        dbWorkerThread = HandlerThread("dbWorker")
        dbWorkerThread.start()
        dbThreadHandler = Handler(dbWorkerThread.looper)

        spinnerArrayAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        spnSearchServers.adapter = spinnerArrayAdapter

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

    /**
     *  This function is called after a search request was successfully fired and persists the summoner to the db
     *
     *  Sends the recentSummoner as object in a Runnable to the ThreadHandler which then persists it
     */
    private fun persistRecentSummoner(summonerName: String, region: String) {
        val summonerToPersist = RecentSummonerData(summonerName = summonerName, region = region)

        // runnable to post to ThreadHandler
        val task = Runnable {
            // Deletes the summoner if he does already exist
            dbInstance.recentSummonerDao().deleteBySummonerName(summonerName)

            // Deletes the "oldest" summoner if there are more than three persisted summoners
            val summonerCount = dbInstance.recentSummonerDao().getCount()
            if (summonerCount >= 3) {
                dbInstance.recentSummonerDao().deleteOldest()
            }
            // Persists the summoner
            dbInstance.recentSummonerDao().insert(summonerToPersist)
        }

        // posts the Task to the ThreadHandler
        dbThreadHandler.looper
        dbThreadHandler.post(task)
    }

    /*
     * Loads the listview adapter for the recent search
     *
     * Puts the loading for the Listview into a Runable task and sends it to the Threadhandler
     */
    private fun setAsyncListViewAdapter() {
        // runnable to post to ThreadHandler
        val task = Runnable {
            val summoners = dbInstance.recentSummonerDao().getAll()

            // parses the summoner names out of the RecentSummoner objects
            val nameList = ArrayList<String>()
            for (summoner in summoners) {
                nameList.add(summoner.summonerName)
            }

            // removes the RecentSearch GUI element if no Summoner is persisted
            if (nameList.size == 0) {
                searchLayout.visibility = View.GONE
            }

            // creates and sets the adapter for the ListView
            val adapter = ArrayAdapter<String>(this, R.layout.recent_list_entry, nameList.reversed())
            listSearchRecent.post {
                listSearchRecent.adapter = adapter
            }

            // sets the onclickListener for the listSearch
            listSearchRecent.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val item = listSearchRecent.getItemAtPosition(position) as String
                    txtSummonerName.setText(item)
                }
        }

        // posts the task to the ThreadHandler after chekcking if the looper is initialized
        dbThreadHandler.looper
        dbThreadHandler.post(task)
    }

    private fun startNavigation(type: NavigationType) {
        if(txtSummonerName.text == null || txtSummonerName.text!!.isEmpty()) {
            Toast.makeText(this, "No summoner name entered", Toast.LENGTH_SHORT).show()
            return
        }

        val summonerName = txtSummonerName.text.toString()

        if(summonerName != DataHolder.summoner.name) {
            //persistRecentSummoner(summonerName, region = "euw1")
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

            persistRecentSummoner(summonerName, "euw1")
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