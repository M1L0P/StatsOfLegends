package ch.noseryoung.statsoflegends.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentSummonerData (
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo
    var summonerName: String,

    @ColumnInfo
    var region: String
)