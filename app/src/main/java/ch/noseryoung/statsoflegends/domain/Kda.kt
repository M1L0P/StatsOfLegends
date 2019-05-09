package ch.noseryoung.statsoflegends.domain

data class Kda (
    var kills: Int,
    var deaths: Int,
    var assists: Int,

    var kda: Float = kills.toFloat().plus(assists.toFloat()).div(deaths.toFloat())
)
