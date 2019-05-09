package ch.noseryoung.statsoflegends.domain

class Summary (
    var matchHistory: MatchHistory,
    var summoner: Summoner,
    var wins: Int,
    var games: Int = matchHistory.matches.size,
    var looses: Int = games-wins,
    var mostPlayedChampion: Champion,
    var seconsMostPlayedChampion: Champion
) {
    init {
        matchHistory.matches.forEach {
            if (it.won) {
                this.wins++
            }
        }
    }
}