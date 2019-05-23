package ch.noseryoung.statsoflegends.persistence

import android.content.Context
import android.os.Handler
import android.os.HandlerThread

object DbManager {
    // lateinit variables for database connection and ThreadHandling
    lateinit var dbInstance: RecentSummonerDb
    lateinit var dbWorkerThread: HandlerThread
    lateinit var dbThreadHandler: Handler

    fun initialize(context: Context) {
        // Initialize database instance and start dbWorker thread
        dbInstance = RecentSummonerDb.getInstance(context)!!
        dbWorkerThread = HandlerThread("dbWorker")
        dbWorkerThread.start()
        dbThreadHandler = Handler(dbWorkerThread.looper)
    }

    /**
     *  This function is called after a search request was successfully fired and persists the summoner to the db
     *
     *  Sends the recentSummoner as object in a Runnable to the ThreadHandler which then persists it
     */
    fun persistRecentSummoner(summonerName: String, region: String) {
        val summonerToPersist = RecentSummonerData(summonerName = summonerName, region = region)

        // runnable to post to ThreadHandler
        val task = Runnable {
            // Deletes the summoner if he does already exist
            dbInstance.recentSummonerDao().deleteBySummonerName(summonerName)

            // Deletes the "oldest" summoner if there are more than three persisted summoners
            val summonerCount = dbInstance.recentSummonerDao().getCount()
            if (summonerCount >= 3) {
                dbInstance.recentSummonerDao().deleteOldest()
            }
            // Persists the summoner
            dbInstance.recentSummonerDao().insert(summonerToPersist)
        }
        postTask(task)
    }

    fun getAll(): List<RecentSummonerData> {
        return dbInstance.recentSummonerDao().getAll()
    }

    fun postTask(task: Runnable) {
        // posts the task to the ThreadHandler after chekcking if the looper is initialized
        dbThreadHandler.looper
        dbThreadHandler.post(task)
    }
}