package ch.noseryoung.statsoflegends.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Data class which declares the database structure
 */
@Entity
data class RecentSummonerData (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo
    var summonerName: String,

    @ColumnInfo
    var region: String
)