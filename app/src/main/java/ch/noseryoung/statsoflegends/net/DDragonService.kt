package ch.noseryoung.statsoflegends.net

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface DDragonService {

    interface GitHubService {
        @GET("users/{user}/repos")
        fun getChampionMapping(): Call<List<Repo>>
    }
}