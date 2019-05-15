package ch.noseryoung.statsoflegends.net

import android.content.Context
import android.util.Log
import ch.noseryoung.statsoflegends.domain.Match
import org.json.JSONArray
import org.json.JSONObject


object APIManager {

    fun getAccountID(name: String): String {
        var returnVal = ""
        val nameGetter = Thread(Runnable {
            val response = HTTPManager.get(
                "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/$name")
            val json = JSONObject(response)
            returnVal = json["accountId"].toString()
        })
        nameGetter.start()
        nameGetter.join()

        return returnVal
    }

    fun getMatchIDs(accountID: String): ArrayList<String> {
        val returnVal = ArrayList<String>()
        val nameGetter = Thread(Runnable {
            val response = HTTPManager.get(
                "https://euw1.api.riotgames.com/lol/match/v4/matchlists/by-account/${accountID}?endIndex=10")
            val json = JSONObject(response)
            for(i in 0..(json["matches"] as JSONArray).length() - 1) {

                returnVal.add(
                        ((json["matches"] as JSONArray)[i] as JSONObject)["gameId"].toString())

                Log.e("MilooliM", returnVal[returnVal.size - 1])
            }
        })
        nameGetter.start()
        nameGetter.join()

        return returnVal
    }

    fun getMatch(context: Context, summonerName: String, matchID: String): Match? {
        var returnVal: Match? = null
        val nameGetter = Thread(Runnable {
            val response = HTTPManager.get(
                "https://euw1.api.riotgames.com/lol/match/v4/matches/${matchID}")
            if(response == null) {
                Log.e("MilooliM", "Failed to get match details")
                return@Runnable
            }
            returnVal = MatchFactory.generate(context, summonerName, response)
        })
        nameGetter.start()
        nameGetter.join()

        return returnVal
    }
}