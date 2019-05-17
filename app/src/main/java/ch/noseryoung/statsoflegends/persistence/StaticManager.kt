package ch.noseryoung.statsoflegends.persistence

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.BlurMaskFilter
import com.squareup.picasso.Picasso
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import android.graphics.drawable.Drawable
import android.util.Log
import ch.noseryoung.statsoflegends.R
import com.squareup.picasso.Target
import kotlin.annotation.Target as Target1


object StaticManager {

    /**
     * Load a champion icon by champion ID
     *
     * @param context Context from which the function gets called
     * @param id ID of the champion e.g. "85"
     */
    fun getChampionIcon(context: Context, id: String) : Bitmap? {
        if(existsLocal("champ_$id.png"))
            return getLocal(context, "champ_$id.png")
        else {
            val bitmap = getOnline(context, "champion/$id")
            if(bitmap != null) persist(context, "champ_$id.png", bitmap)
            return bitmap
        }
    }

    /**
     * Load a champion splash art by champion ID
     *
     * @param context Context from which the function gets called
     * @param id ID of the champion e.g. "85"
     */
    fun getChampionSplash(context: Context, id: String) : Bitmap? {
        val bitmap = getChampionSplashOnline(context, id)
        return bitmap
    }

    /**
     * Load a item image by item ID
     *
     * @param context Context from which the function gets called
     * @param id ID of the item e.g. "823"
     */
    fun getItemIcon(context: Context, id: String) : Bitmap? {
        if(id == "0") return null

        if(existsLocal("item_$id.png")) return getLocal(context, "item_$id.png")
        else {
            val bitmap = getOnline(context, "item/$id")
            if(bitmap != null) persist(context, "item_$id.png", bitmap)
            return bitmap
        }
    }

    /**
     * Load a summoner spell image by spell ID
     *
     * @param context Context from which the function gets called
     * @param id ID of the spell e.g. "123"
     */
    fun getSpellIcon(context: Context, id: String) : Bitmap? {
        if(existsLocal("spell_$id.png")) return getLocal(context, "spell_$id.png")
        else {
            val bitmap = getOnline(context, "spell/$id")
            if(bitmap != null) persist(context, "spell_$id.png", bitmap)
            return bitmap
        }
    }

    /**
     * Load a profile icon by icon ID
     *
     * @param context Context from which the function gets called
     * @param id ID of the icon e.g. "349"
     */
    fun getProfileIcon(context: Context, id: String) : Bitmap? {
        if(existsLocal("profileicon_$id.png")) return getLocal(context, "profileicon_$id.png")
        else {
            val bitmap = getOnline(context, id, R.string.url_profile_icon)
            if(bitmap != null) persist(context, "profileicon_$id.png", bitmap)
            return bitmap
        }
    }

    /**
     * Load a rune image by rune ID
     *
     * @param context Context from which the function gets called
     * @param id ID of the rune e.g. "5005"
     */
    fun getPerkIcon(context: Context, id: String) : Bitmap? {
        if(existsLocal("perk_$id.png")) return getLocal(context, "perk_$id.png")
        else {
            val bitmap = getOnline(context, "icon/$id")
            if(bitmap != null) persist(context, "perk_$id.png", bitmap)
            return bitmap
        }
    }

    /**
     * Checks if an image already exists locally
     *
     * @param path Path to the image e.g. icon_402.png
     * @return Boolean, if image exists
     */
    private fun existsLocal(path: String) : Boolean {
        val file = File(path)
        if(file.exists()) {
            Log.d("MilooliM", "File exists locally")
        }
        return file.exists()
    }

    /**
     * Get an image online via the picasso library
     *
     * @param context Context from which the function gets called
     * @param id ID of the image to get
     * @param urlID ID of the url string
     * @return Bitmap image, if image was not found null
     */
    private fun getOnline(context: Context, id: String, urlId: Int = R.string.url_opgg) : Bitmap? {
        var bmp: Bitmap? = null
        Picasso.with(context)
            .load(context.getString(urlId).replace("{}", id))
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                override fun onBitmapFailed(errorDrawable: Drawable?) {
                    Log.e("MilooliM", "Failed to get bitmap at ${context.getString(R.string.url_static).replace("{}", id)}")
                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    bmp = bitmap
                }
            })
        return bmp
    }

    /**
     * Special function to get champion splash image online
     *
     * Uses a different URL that the other image resource
     *
     * @param context Context from which the function gets called
     * @return Bitmap of image, null if the splash image was not found
     */
    private fun getChampionSplashOnline(context: Context, url: String) : Bitmap? {
        var bmp: Bitmap? = null
        Picasso.with(context)
            .load("https://ddragon.leagueoflegends.com/cdn/img/champion/splash/{}_0.jpg".replace("{}", url))
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                override fun onBitmapFailed(errorDrawable: Drawable?) {
                    Log.e("MilooliM", "Failed to get bitmap at ${context.getString(R.string.url_static).replace("{}", url)}")
                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    //Log.e("MilooliM", "Successfully get bitmap at ${context.getString(R.string.url_static).replace("{}", url)}")
                    bmp = bitmap
                }
            })
        return bmp
    }

    /**
     * Load the image from local storage
     *
     * @param context Context from which the function gets called
     * @param file File path to the image
     * @return Bitmap of image, null if the splash image was not found
     */
    private fun getLocal(context: Context, file: String) : Bitmap? {
        return BitmapFactory.decodeStream(
            BufferedInputStream(FileManager(context).readRaw(file)))
    }

    /**
     * Persist an image on local storage
     *
     * @param context Context from which the function gets called
     * @param path Path to the local image
     * @param bitmap Bitmap data of the image
     */
    private fun persist(context: Context, path: String, bitmap: Bitmap) {
        try {
            Log.d("MilooliM", "Save image to $path")
            context.openFileOutput(path, Context.MODE_PRIVATE).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            Log.e("MilooliM", "Failed to persist the image $path")
        }
    }
}
