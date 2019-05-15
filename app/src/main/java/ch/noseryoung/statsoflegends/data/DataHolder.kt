package ch.noseryoung.statsoflegends.data

import ch.noseryoung.statsoflegends.domain.MatchHistory
import ch.noseryoung.statsoflegends.domain.Summoner
import kotlin.math.roundToInt

object DataHolder {
    var region: String = ""
    var matchHistory: MatchHistory = MatchHistory()
    var summoner: Summoner = Summoner()

    fun clear() {
        summoner = Summoner()
        matchHistory = MatchHistory()
    }

    fun getWins() {

    }

    fun getLooses() {

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

    private fun championFrequencyList() : Set<String> {
        val champList = ArrayList<String>()
        for(match in matchHistory.getMatches()) {
            champList.add(match.championName)
        }

        val champFreqMap = HashMap<String, Int>()
        for(champ in champList) {
            if(!champFreqMap.containsKey(champ)) {
                champFreqMap[champ] = 1
            } else {
                champFreqMap[champ]?.inc()
            }
        }

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