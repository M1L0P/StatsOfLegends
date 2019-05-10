package ch.noseryoung.statsoflegends

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import ch.noseryoung.statsoflegends.net.APIManager
import ch.noseryoung.statsoflegends.net.MatchFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_matchhistory.*


class NavigationActivity : AppCompatActivity() {

    private fun loadMatchHistory() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewFragmentHolder, MatchHistoryFragment())
            .commit()

        Thread(Runnable {
            val accoundId = APIManager.getAccountID("sirtubelujohnson")
            val matchIds = APIManager.getMatchIDs(accoundId)
            for (id in matchIds) {
                val match = APIManager.getMatch(this, "sirtubelujohnson", id)
                if (match != null) {
                    MatchFactory.history.matches.add(match)

                    runOnUiThread {
                        rcyHistory.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }).start()
    }

    private fun loadSummary() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewFragmentHolder, SummaryFragment())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    true
                }
                R.id.navigation_history -> {
                    loadMatchHistory()
                    true
                }
                R.id.navigation_summary -> {
                    loadSummary()
                    true
                }
                else -> false
            }
        }

        // Set selected intend
        when (intent.extras.getInt("type")) {
            NavigationType.HISTORY.value -> {
                loadMatchHistory()
                nav_view.menu.get(1).setChecked(true)
            }
            NavigationType.SUMMARY.value -> {
                loadSummary()
                nav_view.menu.get(2).setChecked(true)
            }
        }
    }
}
