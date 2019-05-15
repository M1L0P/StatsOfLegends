package ch.noseryoung.statsoflegends

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.noseryoung.statsoflegends.data.servers
import ch.noseryoung.statsoflegends.net.APIManager
import ch.noseryoung.statsoflegends.net.HTTPManager.loadMapping
import ch.noseryoung.statsoflegends.persistence.DbWorkerThread
import ch.noseryoung.statsoflegends.persistence.RecentSummonerData
import ch.noseryoung.statsoflegends.persistence.RecentSummonerDb
import kotlinx.android.synthetic.main.activity_search.*



class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val spinnerArrayAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            servers.keys.toList()
        )

        val mobileArray =
            arrayOf("Bilgewaters call", "SirtubelUJohnson", "Jeff")

        val adapter = ArrayAdapter<String>(
            this,
            R.layout.recent_list_entry, mobileArray
        )

        val listView = listSearchRecent
        listView.setAdapter(adapter)


        spinnerArrayAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        spnSearchServers.setAdapter(spinnerArrayAdapter)

        btnSearchHistory.setOnClickListener {
            if(!checkNameExists()) return@setOnClickListener

            startNavigation(NavigationType.HISTORY)

            // Get champion and spell mapping
            loadMapping(this, R.string.url_champmap, R.string.local_champmap)
            loadMapping(this, R.string.url_spellmap, R.string.local_spellmap)
        }

        btnSearchSummary.setOnClickListener {
            if(!checkNameExists()) return@setOnClickListener
            startNavigation(NavigationType.SUMMARY)

            // Get champion and spell mapping
            loadMapping(this, R.string.url_champmap, R.string.local_champmap)
            loadMapping(this, R.string.url_spellmap, R.string.local_spellmap)
        }
    }

    fun persistRecentSummoner(summonerName: String, region: String) {
        val db = RecentSummonerDb.getInstance(this)
        val uiHandler = Handler()

        val dbWorkerThread = DbWorkerThread("dbWorkerThread")
        dbWorkerThread.start()

        val summonerToPersist = RecentSummonerData(summonerName = summonerName, region = region)

        val task = Runnable {
            if(db == null) return@Runnable
            val summonerCount = db.RecentSummonerDao().getCount()
            if (summonerCount > 3) {
                db.RecentSummonerDao().deleteOldest()
            }
            db.RecentSummonerDao().insert(summonerToPersist)
        }
        dbWorkerThread.postTask(task)
    }

    fun startNavigation(type: NavigationType) {
        val intent = Intent(this, NavigationActivity::class.java)
        intent.putExtra("type", type.value)

        startActivity(intent)
    }

    fun checkNameExists(): Boolean {
        val text = "Name does not exist on specified server!"

        val toast = Toast.makeText(this.applicationContext, text, Toast.LENGTH_LONG)
        toast.show()

        return true
    }
}

enum class NavigationType(val value: Int) {
    HISTORY(0),
    SUMMARY(1)
}