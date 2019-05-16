package ch.noseryoung.statsoflegends.data

import android.util.Log
import ch.noseryoung.statsoflegends.domain.MatchHistory
import ch.noseryoung.statsoflegends.domain.Summoner
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.roundToInt

object DataHolder {
    var region: String = ""
    var matchHistory: MatchHistory = MatchHistory()
    var summoner: Summoner = Summoner()

    var threadList: CopyOnWriteArrayList<Thread> = CopyOnWriteArrayList()

    fun clear() {
        summoner = Summoner()
        matchHistory = MatchHistory()
    }

    fun getWins() {

    }

    fun getLooses() {

    }

    fun getPrimaryChampionWinRate() : Int {
        return getWinRate(getPrimaryChampion())
    }

    fun getSecondaryChampionWinRate() : Int {
        return getWinRate(getSecondaryChampion())
    }

    fun getPrimaryChampionRate() : Int {
        return championRate(getPrimaryChampion())
    }

    fun getSecondaryChampionRate() : Int {
        return championRate(getSecondaryChampion())
    }


    fun getPrimaryChampion() : String {
        return championFrequencyList().toList()[0]
    }

    fun getSecondaryChampion(): String {
        val secondaryList = championFrequencyList().toList()
        if(secondaryList.isNotEmpty()) {
            return secondaryList[1]
        }
        return ""
    }

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