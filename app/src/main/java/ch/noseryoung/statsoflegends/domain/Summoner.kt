package ch.noseryoung.statsoflegends.domain

class Summoner {
    var accountId: String = ""
    var summonerId: String = ""
    var name: String = ""
    var nameBeauty: String = ""
    var icon: String = ""
    var level: Int = 0
    var flex: Rank = Rank()
    var solo: Rank = Rank()
}
