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

    // OkHTTP client to use during requests
    var client = OkHttpClient()

    /**
     * Perform a GET request to a given riot endpoint with API key in header
     *
     * This function has to be called outside the main thread
     *
     * @param url Full URL to the endpoint
     * @return HTTP return
     */
    fun get(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .header("X-Riot-Token", "RGAPI-17dcd7fd-0bf4-46a7-bc81-691727e603ac")
            .build()

        return client.newCall(request).execute().body().string()
    }

    /**
     * Load a mapping from DDragon
     *
     * This function has to be called outside the main thread
     *
     * @context Context from which the function was called
     * @mapUrlId ID of the map in url
     * @mapLocalId ID of the map in local storage
     */
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