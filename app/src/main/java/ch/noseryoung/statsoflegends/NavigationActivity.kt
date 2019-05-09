package ch.noseryoung.statsoflegends

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : AppCompatActivity() {

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
                    txtTest.setText(R.string.title_history)
                    true
                }
                R.id.navigation_summary -> {
                    txtTest.setText(R.string.title_summary)
                    true
                }
                else -> false
            }
        }

        // Set selected intend
        when (intent.extras.getInt("type")) {
            NavigationType.HISTORY.value -> {

            }
            NavigationType.SUMMARY.value -> {

            }
        }
    }
}
