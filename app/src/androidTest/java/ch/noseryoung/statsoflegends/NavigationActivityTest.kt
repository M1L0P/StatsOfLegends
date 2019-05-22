package ch.noseryoung.statsoflegends

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import ch.noseryoung.statsoflegends.net.HTTPManager
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NavigationActivityTest {

    private val playerResponse = """
        {
            "profileIconId": 4026,
            "name": "SirTubelUJohnson",
            "puuid": "VGmWwM2Mafqryz1-cdXC7b2i37vAl3aq9iTrLKHE7A2ClwfpfUt2YMVQPzzBkvsBuRlPOlPB_sn2qA",
            "summonerLevel": 35,
            "accountId": "N762uPvX-MknXW7xlndIzB_sCNnWha6zowq_8jeyvvNapKKWFammhUCc",
            "id": "epQhVEfrHvMzLeYEOHWFxE02GH9cILlugB8hMpczC2zJ7WQ5",
            "revisionDate": 1558467645000
        }
    """.trimIndent()

    @Mock
    private lateinit var httpManager: HTTPManager

    private val name = "sirtubelujohnson"
    private val url = "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$name"

    @Rule
    var mActivityRule = ActivityTestRule(
        NavigationActivity::class.java
    )

    @Test
    fun nameFindSuccess() {

        val appContext = InstrumentationRegistry.getInstrumentation().context

        `when`(httpManager.get(url)).thenReturn(playerResponse)

        // Enter summoner name and click button
        Espresso.onView(withId(R.id.txtSummonerName))
            .perform(ViewActions.typeText(name))
        Espresso.onView(withId(R.id.btnSearchSummary))
            .perform(ViewActions.click())

        // Check if bar is correct
        assert(mActivityRule.activity.title == "SirTubelUJohnson")
    }
}

