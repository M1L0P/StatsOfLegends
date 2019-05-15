package ch.noseryoung.statsoflegends.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(RecentSummonerData::class), version = 1)
abstract class RecentSummonerDb : RoomDatabase() {

    abstract fun RecentSummonerDao(): RecentSummonerDao

    companion object {
        private var INSTANCE: RecentSummonerDb? = null

        fun getInstance(context: Context): RecentSummonerDb? {
            if (INSTANCE == null) {
                synchronized(RecentSummonerDb::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        RecentSummonerDb::class.java, "recentSummoner.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}