package ch.noseryoung.statsoflegends.data

import android.util.Log
import ch.noseryoung.statsoflegends.domain.MatchHistory
import ch.noseryoung.statsoflegends.domain.Summoner
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.roundToInt

object DataHolder {
    // Server region e.g. EUW, NA
    var region: String = ""
    var matchHistory: MatchHistory = MatchHistory()
    var summoner: Summoner = Summoner()

    // Contains all currently working threads
    var threadList: CopyOnWriteArrayList<Thread> = CopyOnWriteArrayList()

    /**
     * Clear all data except the region
     */
    fun clear() {
        summoner = Summoner()
        matchHistory = MatchHistory()
    }

    /**
     * Get player overall win rate in percent
     *
     * @return Overall win rate in %
     */
    fun getWinRate() : Int {
        var won = 0

        for(match in matchHistory.getMatches()) {
            if(match.won) won++
        }

        return 100.div(matchHistory.getMatches().size).times(won)
    }

    /**
     * Get primary champion pick rate as percent
     *
     * @return Primary champion pick rate in %
     */
    fun getPrimaryChampionWinRate() : Int {
        return getWinRate(getPrimaryChampion())
    }

    /**
     * Get secondary champion win rate as percent
     *
     * @return Secondary champion win rate in %
     */
    fun getSecondaryChampionWinRate() : Int {
        return getWinRate(getSecondaryChampion())
    }

    /**
     * Get primary champion pick rate as percent
     *
     * @return Primary champion pick rate in %
     */
    fun getPrimaryChampionPickRate() : Int {
        return championRate(getPrimaryChampion())
    }

    /**
     * Get secondary champion pick rate as percent
     *
     * @return Secondary champion pick rate in %
     */
    fun getSecondaryChampionPickRate() : Int {
        return championRate(getSecondaryChampion())
    }


    /**
     * Get most played champion of current summoner
     *
     * @return Primary champion name
     */
    fun getPrimaryChampion() : String {
        val secondaryList = championFrequencyList().toList()
        if(secondaryList.isNotEmpty()) {
            return secondaryList[0]
        }
        return ""
    }

    /**
     * Get second most played champion of current summoner
     *
     * @return Secondary champion name
     */
    fun getSecondaryChampion(): String {
        val secondaryList = championFrequencyList().toList()
        if(secondaryList.isNotEmpty()) {
            return secondaryList[1]
        }
        return ""
    }

    /**
     * Get win rate of current summoner in percent
     *
     * @return Win rate in %
     */
    private fun getWinRate(champion: String) : Int {
        var won = 0
        var played = 0

        for(match in matchHistory.getMatches()) {
            if(match.championName == champion) {
                played++
                if(match.won) {
                    won++
                }
            }
        }

        return 100.div(played).times(won)
    }

    /**
     * Get champion pick rate frequency as list
     *
     * List is sorted by frequency:
     * e.g. ["Heimerdinger", "Vayne", "Kennen", "Kayle"]
     *
     * @return Set of strings as shown above
     */
    private fun championFrequencyList() : Set<String> {
        val champList = ArrayList<String>()
        for(match in matchHistory.getMatches()) {
            if(!match.championName.isEmpty()) {
                champList.add(match.championName)
            }
        }

        val champFreqMap = HashMap<String, Int>()
        for(champ in champList) {
            if(!champFreqMap.containsKey(champ)) {
                champFreqMap[champ] = 1
            } else {
                champFreqMap[champ] = champFreqMap[champ]!!.plus(1)
            }
        }

        Log.e("MilooliM", champFreqMap.toString())
        Log.e("MilooliM", champFreqMap.toList().sortedBy { (_, value) -> value}.reversed().toMap().keys.toString())

        return champFreqMap.toList().sortedBy { (_, value) -> value}.reversed().toMap().keys
    }

    /**
     * Get pick rate of specific champion as percent
     *
     * @param champ Champion name
     * @return Pick rate of champion in match history
     */
    private fun championRate(champ: String) : Int {
        var gamesWith = 0
        var gamesWithout = 0

        for(match in matchHistory.getMatches()) {
            if(match.championName == champ)
                gamesWith++
            else
                gamesWithout++

        }

        return 100f.div(gamesWith+gamesWithout).times(gamesWith).roundToInt()
    }
}