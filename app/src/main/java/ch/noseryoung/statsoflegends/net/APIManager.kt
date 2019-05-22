package ch.noseryoung.statsoflegends.net

import android.content.Context
import android.util.Log
import ch.noseryoung.statsoflegends.data.DataHolder
import ch.noseryoung.statsoflegends.domain.Match
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class APIManager {

    /**
     * Fetch all data from the endpoints
     *
     * Returns true if the searched summoner was found and
     * continues getting everything in the background.
     * If the summoner name was not found, the function
     * returns false and no background threads will be started.
     *
     * @param context Context to use
     * @param name Name of the summoner
     * @return Boolean, if the summoner name was found
     */
    fun fetch(context: Context, name: String) : Boolean {
        // Kill all running threads
        for(thread in DataHolder.threadList) {
            if(thread.isAlive) {
                thread.interrupt()
                thread.join()
            }
            DataHolder.threadList.remove(thread)
        }

        // Clear existing data
        DataHolder.clear()

        var response: String? = null
        val json: JSONObject

        // Get the summoner json
        DataHolder.summoner.name = name
        val nameGetter = Thread(Runnable {
            response = HTTPManager().get(
                "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$name"
            )
        })
        DataHolder.threadList.add(nameGetter)
        nameGetter.start()
        try {
            nameGetter.join()
        } catch (ex: InterruptedException) {}


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

        // Summoner exists, proceed to get all data
        val accountGetter = Thread(Runnable {
            val summonerGetter = Thread(Runnable {
                try {
                    DataHolder.summoner.accountId = json["accountId"].toString()
                    DataHolder.summoner.summonerId = json["id"].toString()
                    DataHolder.summoner.nameBeauty = json["name"].toString()
                    DataHolder.summoner.icon = json["profileIconId"].toString()
                    DataHolder.summoner.level = Integer.parseInt(json["summonerLevel"].toString())
                    getRanks()
                } catch (ex: JSONException) {}
            })
            DataHolder.threadList.add(summonerGetter)
            summonerGetter.start()
            summonerGetter.join()

            val matchGetter = Thread(Runnable {
                loadMatches(context)
            })
            DataHolder.threadList.add(matchGetter)
            matchGetter.start()
        })
        DataHolder.threadList.add(accountGetter)
        accountGetter.start()

        return true
    }

    /**
     * Load the ranks (solo/duo and flex) of a summoner
     */
    private fun getRanks() {
        val accountId = DataHolder.summoner.accountId
        if(accountId.isEmpty()) {
            Log.e("MilooliM", "Failed to get match IDs (Account null)")
            return
        }

        val thread = Thread(Runnable {
            val response = HTTPManager().get(
                "https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/${DataHolder.summoner.summonerId}")
            if(response == null) {
                Log.e("MilooliM", "Failed to get rank details")
                return@Runnable
            }

            val json = JSONArray(response)
            if(json.length() == 0) {
                Log.d("MilooliM", "Unranked player found: "+DataHolder.summoner.name)
                DataHolder.summoner.solo.tier = "Unranked"
                DataHolder.summoner.flex.tier = "Unranked"
                return@Runnable
            }
            try {
                for(i: Int in 0..json.length()) {
                    val obj = json[i] as JSONObject
                    if(obj["queueType"].toString().contains("RANKED_SOLO")) {
                        DataHolder.summoner.solo.rank = obj["rank"].toString()
                        DataHolder.summoner.solo.tier = obj["tier"].toString()
                        DataHolder.summoner.solo.leaguePoints = obj["leaguePoints"].toString()
                    } else if (obj["queueType"].toString().contains("RANKED_FLEX")) {
                        DataHolder.summoner.flex.rank = obj["rank"].toString()
                        DataHolder.summoner.flex.tier = obj["tier"].toString()
                        DataHolder.summoner.flex.leaguePoints = obj["leaguePoints"].toString()
                    }
                }
            } catch (ex: JSONException) {
                Log.e("MilooliM", "Failed to get rank details")
                return@Runnable
            }
        })
        DataHolder.threadList.add(thread)
        thread.start()
        try {
            thread.join()
        } catch (ex: InterruptedException) {}
    }

    /**
     * Load the matches from a specific summoner
     *
     * The summoner account ID is obtained through the global data holder
     *
     * @context Context from where the function is called
     */
    private fun loadMatches(context: Context) {
        val accountId = DataHolder.summoner.accountId
        Log.e("MilooliM", "Initial account ID: "+DataHolder.summoner.accountId)
        if(accountId.isEmpty()) {
            Log.e("MilooliM", "Failed to get match IDs (Account null)")
            return
        }

        val thread = Thread(Runnable {
            val response = HTTPManager().get(
                "https://euw1.api.riotgames.com/lol/match/v4/matchlists/by-account/${accountId}?endIndex=20")
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
                return@Runnable
            }
        })
        DataHolder.threadList.add(thread)
        thread.start()
        try {
            thread.join()
        } catch (ex: InterruptedException) {}
    }


    /**
     * Get details of one match
     *
     * @param context Context from which the function was called
     * @param matchID Match ID to use while fetching information
     * @return A filled out match object
     */
    private fun getMatch(context: Context, matchID: String): Match? {
        var returnVal: Match? = null
        val thread = Thread(Runnable {
            val response = HTTPManager().get(
                "https://euw1.api.riotgames.com/lol/match/v4/matches/${matchID}")
            if(response == null) {
                Log.e("MilooliM", "Failed to get match details")
                return@Runnable
            }
            returnVal = MatchFactory.generate(context, DataHolder.summoner.accountId, response)
        })
        DataHolder.threadList.add(thread)
        thread.start()
        try {
            thread.join()
        } catch (ex: InterruptedException) {}


        return returnVal
    }
}