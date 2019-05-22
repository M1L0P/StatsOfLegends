package ch.noseryoung.statsoflegends.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
  * Singleton to handle db requests
  */
@Database(entities = [RecentSummonerData::class], version = 1)
abstract class RecentSummonerDb : RoomDatabase() {

    abstract fun recentSummonerDao(): RecentSummonerDao

    companion object {
        private var INSTANCE: RecentSummonerDb? = null

        fun getInstance(context: Context): RecentSummonerDb? {
            if (INSTANCE == null) {
                synchronized(RecentSummonerDb::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RecentSummonerDb::class.java, "recentSummoner.db")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}