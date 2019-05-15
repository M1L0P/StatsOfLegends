package ch.noseryoung.statsoflegends.net

import android.content.Context
import android.util.Log
import ch.noseryoung.statsoflegends.data.DataHolder
import ch.noseryoung.statsoflegends.domain.Match
import ch.noseryoung.statsoflegends.domain.Summoner
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object APIManager {

    fun fetch(context: Context, name: String) : Boolean {
        // Clear existing data
        DataHolder.clear()

        var success = true
        var response: String? = null
        var json = JSONObject()

        DataHolder.summoner.name = name
        val nameGetter = Thread(Runnable {
            response = HTTPManager.get(
                "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$name"
            )
        })
        nameGetter.start()
        nameGetter.join()

        // Check if summoner exists
        try {
            json = JSONObject(response)
            if (json["accountId"].toString() == "") {
                return false
            }
        } catch (ex: JSONException) {
            DataHolder.clear()
            return false
        }

        // Get all data
        Thread(Runnable {
            val summonerGetter = Thread(Runnable {
                try {
                    DataHolder.summoner.accountId = json["accountId"].toString()
                    DataHolder.summoner.summonerId = json["id"].toString()
                    DataHolder.summoner.nameBeauty = json["name"].toString()
                    DataHolder.summoner.icon = json["profileIconId"].toString()
                    DataHolder.summoner.level = Integer.parseInt(json["summonerLevel"].toString())
                    success = getRanks()
                } catch (ex: JSONException) {
                    success = false
                }
            })
            summonerGetter.start()
            summonerGetter.join()

            Thread(Runnable {
                success = loadMatches(context)
            }).start()
        }).start()

        return true
    }

    private fun getRanks() : Boolean {
        val accountId = DataHolder.summoner.accountId
        if(accountId.isEmpty()) {
            Log.e("MilooliM", "Failed to get match IDs (Account null)")
            return false
        }

        var success = true

        val nameGetter = Thread(Runnable {
            val response = HTTPManager.get(
                "https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/${DataHolder.summoner.summonerId}")
            if(response == null) {
                Log.e("MilooliM", "Failed to get rank details")
                return@Runnable
            }

            val json = JSONArray(response)
            try {
                for(i: Int in 0..json.length()) {
                    val obj = json[i] as JSONObject
                    if(obj["queueType"].toString().contains("RANKED_SOLO")) {
                        DataHolder.summoner.solo.rank = obj["queueType"].toString()
                        DataHolder.summoner.solo.tier = obj["tier"].toString()
                    } else if (obj["queueType"].toString().contains("RANKED_FLEX")) {
                        DataHolder.summoner.flex.rank = obj["queueType"].toString()
                        DataHolder.summoner.flex.tier = obj["tier"].toString()
                    }
                }
            } catch (ex: JSONException) {
                Log.e("MilooliM", "Failed to get rank details")
                success = false
                return@Runnable
            }
        })
        nameGetter.start()
        nameGetter.join()

        return success
    }

    private fun loadMatches(context: Context) : Boolean {
        val accountId = DataHolder.summoner.accountId
        Log.e("MilooliM", "Initial account ID: "+DataHolder.summoner.accountId)
        if(accountId.isEmpty()) {
            Log.e("MilooliM", "Failed to get match IDs (Account null)")
            return false
        }

        var success = true

        val nameGetter = Thread(Runnable {
            val response = HTTPManager.get(
                "https://euw1.api.riotgames.com/lol/match/v4/matchlists/by-account/${accountId}?endIndex=10")
            val json = JSONObject(response)
            try {
                for (i in 0 until (json["matches"] as JSONArray).length()) {
                    val matchId = ((json["matches"] as JSONArray)[i] as JSONObject)["gameId"].toString()
                    val match = getMatch(context, matchId)
                    if(match != null) {
                        DataHolder.matchHistory.addMatch(match)
                    } else {
                        Log.w("MilooliM", "Skipping a failed match")
                    }
                }
            } catch (ex: JSONException) {
                Log.e("MilooliM", "Failed to get match")
                success = false
                return@Runnable
            }
        })
        nameGetter.start()
        nameGetter.join()

        return success
    }


    private fun getMatch(context: Context, matchID: String): Match? {
        var returnVal: Match? = null
        val nameGetter = Thread(Runnable {
            val response = HTTPManager.get(
                "https://euw1.api.riotgames.com/lol/match/v4/matches/${matchID}")
            if(response == null) {
                Log.e("MilooliM", "Failed to get match details")
                return@Runnable
            }
            returnVal = MatchFactory.generate(context, DataHolder.summoner.accountId, response)
        })
        nameGetter.start()
        nameGetter.join()

        return returnVal
    }
}