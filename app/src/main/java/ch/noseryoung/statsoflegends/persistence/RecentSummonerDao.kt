package ch.noseryoung.statsoflegends.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentSummonerDao {
    @Query("SELECT id, summonerName, region FROM RecentSummonerData")
    fun getAll(): List<RecentSummonerData>

    @Insert(onConflict = 1)
    fun insert(recentSummonerData: RecentSummonerData)

    @Query("DELETE FROM RecentSummonerdata WHERE id = (SELECT MIN(Id) FROM RecentSummonerData)")
    fun deleteOldest()

    @Query("SELECT count(id) FROM RecentSummonerData")
    fun getCount(): Int

    @Query("DELETE FROM RecentSummonerData WHERE summonerName=:summonerName")
    fun deleteBySummonerName(summonerName: String)
}