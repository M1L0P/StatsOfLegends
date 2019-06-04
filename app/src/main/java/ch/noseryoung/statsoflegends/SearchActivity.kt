package ch.noseryoung.statsoflegends

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.noseryoung.statsoflegends.data.DataHolder
import ch.noseryoung.statsoflegends.data.servers
import ch.noseryoung.statsoflegends.net.APIManager
import kotlinx.android.synthetic.main.activity_search.*
import android.widget.AdapterView
import ch.noseryoung.statsoflegends.net.HTTPManager
import ch.noseryoung.statsoflegends.persistence.DbManager


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val spinnerArrayAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            servers.keys.toList()
        )

        DbManager.initialize(this)

        spinnerArrayAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        spnSearchServers.adapter = spinnerArrayAdapter

        btnSearchHistory.setOnClickListener {
            startNavigation(NavigationType.HISTORY)

            // Get champion and spell mapping
            HTTPManager().loadMapping(this, R.string.url_champmap, R.string.local_champmap)
            HTTPManager().loadMapping(this, R.string.url_spellmap, R.string.local_spellmap)
        }

        btnSearchSummary.setOnClickListener {
            startNavigation(NavigationType.SUMMARY)

            // Get champion and spell mapping
            HTTPManager().loadMapping(this, R.string.url_champmap, R.string.local_champmap)
            HTTPManager().loadMapping(this, R.string.url_spellmap, R.string.local_spellmap)
        }
    }

    override fun onResume() {
        super.onResume()
        setAsyncListViewAdapter()
    }


    /*
     * Loads the listview adapter for the recent search
     *
     * Puts the loading for the Listview into a Runable task and sends it to the Threadhandler
     */
    private fun setAsyncListViewAdapter() {
        // runnable to post to ThreadHandler
        val task = Runnable {
            val summoners = DbManager.getAll()

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

        DbManager.postTask(task)
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
                searchSuceeded = APIManager().fetch(this, summonerName)
            })

            nameCheckThread.start()
            nameCheckThread.join()

            if (!searchSuceeded) {
                Toast.makeText(this, "Name does not exist on specified server", Toast.LENGTH_LONG).show()
                return
            }

            DbManager.persistRecentSummoner(summonerName, "euw1")
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