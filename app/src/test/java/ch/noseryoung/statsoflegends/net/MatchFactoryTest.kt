package ch.noseryoung.statsoflegends.net

import android.content.Context
import ch.noseryoung.statsoflegends.persistence.FileManager
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class MatchFactoryTest {

    @Mock
    private lateinit var fileManager: FileManager
    @Mock
    private lateinit var context: Context

    private val matchJson = """ {
        "queueId": 420,
        "gameId": 4030383995,
        "participantIdentities": [
          {
            "player": {
              "summonerName": "Bilgewaters call",
              "summonerId": "23Sp99vm0nIdHnNfsFFHZ_ivNjBZnhu4cfy8letWSIry4W0",
              "accountId": "Yz5-lWgEzIyHVRjbk_9TYTKhmf_qsRnaLnNh9fJ_FR97ag"
            },
            "participantId": 7
          }
        ],
        "participants": [
          {
            "stats": {
              "kills": 2,
              "item2": 1001,
              "item3": 1055,
              "item0": 3146,
              "item1": 2031,
              "item6": 3340,
              "item4": 3191,
              "item5": 2424,
              "perk0": 8359,
              "assists": 1,
              "win": true,
              "deaths": 3
            },
            "spell1Id": 4,
            "participantId": 7,
            "spell2Id": 12,
            },
            "championId":
          }
        ]
        }
    """.trimIndent()

    private val championMap = """
    {
      "type": "champion",
      "format": "standAloneComplex",
      "version": "6.24.1",
      "data": {
            "Kennen": {
          "version": "6.24.1",
          "id": "Kennen",
          "key": "85",
          "name": "Kennen",
          "title": "the Heart of the Tempest",
          "blurb": "There exists an ancient order originating in the Ionian Isles dedicated to the preservation of balance. Order, chaos, light, darkness -- all things must exist in perfect harmony for such is the way of the universe. This order is known as the Kinkou ...",
          "info": {
            "attack": 6,
            "defense": 4,
            "magic": 7,
            "difficulty": 4
          },
          "image": {
            "full": "Kennen.png",
            "sprite": "champion1.png",
            "group": "champion",
            "x": 48,
            "y": 96,
            "w": 48,
            "h": 48
          },
          "tags": [
            "Mage",
            "Marksman"
          ],
          "partype": "Energy",
          "stats": {
            "hp": 535.72,
            "hpperlevel": 79.0,
            "mp": 200.0,
            "mpperlevel": 0.0,
            "movespeed": 335.0,
            "armor": 24.3,
            "armorperlevel": 3.75,
            "spellblock": 30.0,
            "spellblockperlevel": 0.0,
            "attackrange": 550.0,
            "hpregen": 5.59,
            "hpregenperlevel": 0.65,
            "mpregen": 50.0,
            "mpregenperlevel": 0.0,
            "crit": 0.0,
            "critperlevel": 0.0,
            "attackdamage": 50.544,
            "attackdamageperlevel": 3.3,
            "attackspeedoffset": -0.0947,
            "attackspeedperlevel": 3.4
          }
        }
    }
    """.trimIndent()


    @Test
    fun matchFactory_generate_returnsCorrectMatch() {

        `when`(context.getString(Mockito.anyInt())).thenReturn("champion_map.json")
        `when`(fileManager.read("champion_map.json")).thenReturn(championMap)

        val match = MatchFactory.generate(
            context, "Yz5-lWgEzIyHVRjbk_9TYTKhmf_qsRnaLnNh9fJ_FR97ag", matchJson)


        Assert.assertEquals(match.championName, "Kennen")


    }
}