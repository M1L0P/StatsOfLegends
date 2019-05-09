package ch.noseryoung.statsoflegends.domain

data class Summoner (
    var icon: Int,
    var name: String,
    var level: Int,
    var flex: Rank,
    var solo: Rank
)
