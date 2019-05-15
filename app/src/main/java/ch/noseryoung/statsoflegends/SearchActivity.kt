package ch.noseryoung.statsoflegends

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.noseryoung.statsoflegends.data.servers
import ch.noseryoung.statsoflegends.net.APIManager
import ch.noseryoung.statsoflegends.net.HTTPManager.loadMapping
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val spinnerArrayAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item,
            servers.keys.toList()
        )
        spinnerArrayAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        spnServers.setAdapter(spinnerArrayAdapter)

        searchBtnHistory.setOnClickListener {
            startNavigation(NavigationType.HISTORY)

            // Get champion and spell mapping
            loadMapping(this, R.string.url_champmap, R.string.local_champmap)
            loadMapping(this, R.string.url_spellmap, R.string.local_spellmap)
        }

        searchBtnSummary.setOnClickListener {
            startNavigation(NavigationType.SUMMARY)

            // Get champion and spell mapping
            loadMapping(this, R.string.url_champmap, R.string.local_champmap)
            loadMapping(this, R.string.url_spellmap, R.string.local_spellmap)
        }
    }

    fun startNavigation(type: NavigationType) {
        if(txtSummonerName.text == null || txtSummonerName.text!!.isEmpty()) {
            Toast.makeText(this, "No summoner name entered", Toast.LENGTH_SHORT).show()
            return
        }

        val summonerName = txtSummonerName.text.toString()
        var accountId = ""

        val nameCheckThread = Thread(Runnable {
            accountId = APIManager.getAccountID(summonerName)
            if(accountId.isEmpty()) {
                return@Runnable
            }
        })

        nameCheckThread.start()
        nameCheckThread.join()

        if(accountId.isEmpty()) {
            Toast.makeText(this, "Name does not exist on specified server", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, NavigationActivity::class.java)
        intent.putExtra("type", type.value)
        intent.putExtra("accountId", accountId)

        startActivity(intent)
    }
}

enum class NavigationType(val value: Int) {
    HISTORY(0),
    SUMMARY(1)
}