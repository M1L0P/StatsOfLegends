package ch.noseryoung.statsoflegends.persistence

import android.content.Context
import androidx.room.Room
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import androidx.test.rule.ActivityTestRule
import ch.noseryoung.statsoflegends.SearchActivity
import org.junit.Assert.assertEquals
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

    @get:Rule
    var mActivityRule = ActivityTestRule(
        SearchActivity::class.java
    )

    @Before
    fun createDb() {
        val context = mActivityRule.activity.applicationContext
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
    fun writeAndReadSummoner() {
        val summoner: RecentSummonerData = RecentSummonerData(10, "jeff", "EUW")
        recentSummonerDao.insert(summoner)
        val returnedSummoner = recentSummonerDao.getAll()

        assertThat(returnedSummoner.first()  , equalTo(summoner))
    }

    @Test
    @Throws(Exception::class)
    fun deleteOldestSummoner() {
        val summonerOld: RecentSummonerData = RecentSummonerData(10, "jeff", "EUW")
        val summonerNew: RecentSummonerData = RecentSummonerData(11, "jeff", "EUW")
        recentSummonerDao.insert(summonerOld)
        recentSummonerDao.insert(summonerNew)

        recentSummonerDao.deleteOldest()
        val returnedSummoner = recentSummonerDao.getAll()

        assertThat(returnedSummoner.first(), equalTo(summonerNew))
        assertThat(returnedSummoner.size, equalTo(1))
    }
}