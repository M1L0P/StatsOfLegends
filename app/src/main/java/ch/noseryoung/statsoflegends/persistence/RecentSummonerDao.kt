package ch.noseryoung.statsoflegends.persistence

import androidx.room.Insert
import androidx.room.Query

interface RecentSummonerDao {
    @Query("SELECT summonerName, region FROM RecentSummonerData")
    fun getAll(): List<RecentSummonerDao>

    @Insert(onConflict = 1)
    fun insert(recentSummonerData: RecentSummonerData)

    @Query("DELETE from RecentSummonerdata WHERE id = MAX(id)")
    fun deleteOldest()

    @Query("SELECT count(id) FROM RecentSummonerData")
    fun getCount(): Int
}