package ch.noseryoung.statsoflegends.domain

data class Match(
    var championID: Int,
    var kda: Kda,
    var items: Array<Int>,
    var keystone: Int,
    var spieltyp: Int,
    var won: Boolean
);
