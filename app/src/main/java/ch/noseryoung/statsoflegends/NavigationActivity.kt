package ch.noseryoung.statsoflegends

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import ch.noseryoung.statsoflegends.net.APIManager
import ch.noseryoung.statsoflegends.net.MatchFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_matchhistory.*


class NavigationActivity : AppCompatActivity() {

    private var accountId: String? = null

    private fun loadMatchHistory(summonerName: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewFragmentHolder, MatchHistoryFragment())
            .commit()

        Thread(Runnable {
            val matchIds = APIManager.getMatchIDs(accountId!!)
            if(matchIds.isEmpty()) {
                Toast.makeText(this, "Failed to get matches ($summonerName)", Toast.LENGTH_LONG).show()
                return@Runnable
            }
            for (id in matchIds) {
                val match = APIManager.getMatch(this, summonerName, id)
                if (match != null) {
                    MatchFactory.history.addMatch(match)

                    runOnUiThread {
                        rcyHistory.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }).start()
    }

    private fun loadSummary(summonerName: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewFragmentHolder, SummaryFragment())
            .commit()
    }

    private fun getAccountID() {
        this.accountId = intent.extras.getString("accountId")
        if(this.accountId == null) {
            this.finish()
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Load the summoner name from intent
        getAccountID()

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    true
                }
                R.id.navigation_history -> {
                    loadMatchHistory(this.accountId!!)
                    true
                }
                R.id.navigation_summary -> {
                    loadSummary(this.accountId!!)
                    true
                }
                else -> false
            }
        }

        // Set selected intend
        when (intent.extras.getInt("type")) {
            NavigationType.HISTORY.value -> {
                loadMatchHistory(this.accountId!!)
                nav_view.menu.get(1).setChecked(true)
            }
            NavigationType.SUMMARY.value -> {
                loadSummary(this.accountId!!)
                nav_view.menu.get(2).setChecked(true)
            }
        }
    }
}
