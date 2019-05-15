package ch.noseryoung.statsoflegends.data

import ch.noseryoung.statsoflegends.domain.MatchHistory
import ch.noseryoung.statsoflegends.domain.Summoner

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

    fun getPrimaryChampion() {

    }

    fun getSecondaryChampion() {

    }
}