package ch.noseryoung.statsoflegends.net

import android.content.Context
import android.util.Log
import ch.noseryoung.statsoflegends.R
import ch.noseryoung.statsoflegends.data.gameTypes
import ch.noseryoung.statsoflegends.domain.Kda
import ch.noseryoung.statsoflegends.domain.Match
import ch.noseryoung.statsoflegends.persistence.FileManager
import org.json.JSONArray
import org.json.JSONObject


object MatchFactory {

    /**
     * Generates a match from the match JSON return of the endpoint
     *
     * @param context Context to use to read the mapping file
     * @param accountId ID of the account to search for
     * @param jsonString Match as json return of endpoint
     * @return Filled match object generated from the json
     */
    fun generate(context: Context, accountId: String, jsonString: String) : Match {
        val json = JSONObject(jsonString)

        var participantId: String? = null

        // Find participant ID
        val participantIds = json["participantIdentities"] as JSONArray
        for(i in 0 until participantIds.length()) {
            val player = (participantIds[i] as JSONObject)["player"] as JSONObject

            Log.e("MilooliM", "ID: "+player["accountId"].toString().toLowerCase())
            Log.e("MilooliM", "AccountID: "+accountId.toLowerCase())

            if(player["accountId"].toString().toLowerCase() == accountId.toLowerCase()) {
                participantId = (participantIds[i] as JSONObject)["participantId"].toString()
                break
            }
        }
        if(participantId == null) {
            Log.e("MilooliM", "Failed to get participant ID for player ${accountId}")
            throw Exception()
        }

        var participant: JSONObject? = null

        // Find participant
        val participants = json["participants"] as JSONArray
        for(i in 0 until participants.length()) {
            if((participants[i] as JSONObject)["participantId"].toString() == participantId) {
                participant = (participants[i] as JSONObject)
                break
            }
        }
        if(participant == null) {
            Log.e("MilooliM", "Failed to get participant for player ${accountId}")
            throw Exception()
        }

        val stats = participant["stats"] as JSONObject
        val championId = getNameByIdFromMap(context, participant["championId"].toString(), R.string.local_champmap)

        // Calculate API
        val kda = Kda(
            stats["kills"] as Int,
            stats["deaths"] as Int,
            stats["assists"] as Int)
        val won = stats["win"].toString().toBoolean()

        // Keystone rune
        val keystoneId = stats["perk0"].toString()

        // Get game type
        var gameType = gameTypes[json["queueId"] as Int]
        if(gameType == null) gameType = "Other"

        // Set the six items of the summoner
        val items = ArrayList<String>()
        items.add(stats["item0"].toString())
        items.add(stats["item1"].toString())
        items.add(stats["item2"].toString())
        items.add(stats["item3"].toString())
        items.add(stats["item4"].toString())
        items.add(stats["item5"].toString())

        // Set the two summoner spells
        val summonerSpell1 = getNameByIdFromMap(context, participant["spell1Id"].toString(), R.string.local_spellmap)
        val summonerSpell2 = getNameByIdFromMap(context, participant["spell2Id"].toString(), R.string.local_spellmap)

        return Match(
            json["gameId"].toString(), championId,
            kda, items, keystoneId, gameType, won,
            Pair(summonerSpell1, summonerSpell2))
    }

    /**
     * Convert a champion ID to champion name
     *
     * This function gets the champion name by champion ID through
     * the mapping file. Be sure that the mapping is loaded before
     * calling this function.
     *
     * @param context Context to use to read mapping file
     * @param id ID of the champion
     * @param mapId ID of the map to use to convert
     */
    private fun getNameByIdFromMap(context: Context, id: String, mapId: Int) : String {
        val map = FileManager.read(context, context.getString(mapId))

        val data = JSONObject(map)["data"] as JSONObject

        // Iterate through champions and find right one
        val iter = data.keys()
        while (iter.hasNext()) {
            val key = iter.next()
            val value = data.get(key) as JSONObject
            if(value["key"].toString() == id) {
                return key
            }
        }
        return ""
    }
}