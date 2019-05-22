package ch.noseryoung.statsoflegends.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class RecentSummonerDbTest {
    private lateinit var recentSummonerDao: RecentSummonerDao
    private lateinit var db: RecentSummonerDb

    @Rule
    var mActivityRule = ActivityTestRule(
        MainActivity::class.java
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RecentSummonerDb::class.java).build()
        recentSummonerDao = db.recentSummonerDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeSummonerAndReadInList() {
        val summoner: RecentSummonerData = RecentSummonerData(summonerName = "jeff", region = "EUW")
        recentSummonerDao.insert(summoner)
        val returnedSummoner = recentSummonerDao.getAll()

        assertThat(returnedSummoner[0], equalTo(summoner))
    }
}
