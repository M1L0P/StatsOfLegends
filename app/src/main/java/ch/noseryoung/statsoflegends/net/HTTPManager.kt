package ch.noseryoung.statsoflegends.net

import android.content.Context
import android.util.Log
import ch.noseryoung.statsoflegends.persistence.FileManager
import com.squareup.okhttp.Callback
import com.squareup.okhttp.Request
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Response
import java.io.IOException

object HTTPManager {

    var client = OkHttpClient()

    fun get(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .header("X-Riot-Token", "RGAPI-8893c80c-65f0-4ccc-a979-8a6526ea0826")
            .build()

        return client.newCall(request).execute().body().string()
    }

    fun loadMapping(context: Context, mapUrlId: Int, mapLocalId: Int) {
        val map = context.resources.getString(mapUrlId)
        val request = Request.Builder()
            .url(map)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Log.e("MilooliM", "Failed to get map ${map}", e)
            }

            override fun onResponse(response: Response?) {
                if(response == null) {
                    Log.e("Failed to get map", map)
                    return
                }
                FileManager(context).write(context.resources.getString(mapLocalId), response.body().string())
            }
        })
    }
}