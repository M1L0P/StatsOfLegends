package ch.noseryoung.statsoflegends.domain

data class Match(
    var matchId: String,
    var championName: String,
    var kda: Kda,
    var itemID: ArrayList<String>,
    var keyStoneID: String,
    var gameType: String,
    var won: Boolean,
    var summonerSpellIDs: Pair<String, String>
)