package ch.noseryoung.statsoflegends.net

import android.content.res.Resources
import ch.noseryoung.statsoflegends.R

object APIManager {
    /*
     * 1. Get account ID by name
     * Matches:
     *  1. Get Matches by account ID
     */

    val champions = Resources.getSystem().getString(R.string.url_champmap)

    private fun getChampionNameByID(id: Int) : String {

    }
}