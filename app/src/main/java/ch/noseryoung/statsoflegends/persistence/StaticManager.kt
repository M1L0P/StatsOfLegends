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

    fun getChampionIcon(context: Context, id: String) : Bitmap? {
        if(existsLocal("champ_$id.png"))
            return getLocal(context, "champ_$id.png")
        else {
            val bitmap = getOnline(context, "champion/$id")
            if(bitmap != null) persist(context, "champ_$id.png", bitmap)
            return bitmap
        }
    }

    fun getChampionSplash(context: Context, id: String) : Bitmap? {
        val bitmap = getChampionSplashOnline(context, id)
        return bitmap
    }

    fun getItemIcon(context: Context, id: String) : Bitmap? {
        if(id == "0") return null

        if(existsLocal("item_$id.png")) return getLocal(context, "item_$id.png")
        else {
            val bitmap = getOnline(context, "item/$id")
            if(bitmap != null) persist(context, "item_$id.png", bitmap)
            return bitmap
        }
    }

    fun getSpellIcon(context: Context, id: String) : Bitmap? {
        if(existsLocal("spell_$id.png")) return getLocal(context, "spell_$id.png")
        else {
            val bitmap = getOnline(context, "spell/$id")
            if(bitmap != null) persist(context, "spell_$id.png", bitmap)
            return bitmap
        }
    }

    fun getProfileIcon(context: Context, id: String) : Bitmap? {
        if(existsLocal("profileicon_$id.png")) return getLocal(context, "profileicon_$id.png")
        else {
            val bitmap = getOnline(context, "profileicon/$id")
            if(bitmap != null) persist(context, "profileicon_$id.png", bitmap)
            return bitmap
        }
    }

    fun getPerkIcon(context: Context, id: String) : Bitmap? {
        if(existsLocal("perk_$id.png")) return getLocal(context, "perk_$id.png")
        else {
            val bitmap = getOnline(context, "icon/$id")
            if(bitmap != null) persist(context, "perk_$id.png", bitmap)
            return bitmap
        }
    }

    // Check if an image already exists in cache
    private fun existsLocal(path: String) : Boolean {
        val file = File(path)
        return file.exists()
    }

    private fun getOnline(context: Context, url: String) : Bitmap? {
        var bmp: Bitmap? = null
        Picasso.with(context)
            .load(context.getString(R.string.url_static).replace("{}", url))
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
                    Log.e("MilooliM", "Successfully get bitmap at ${context.getString(R.string.url_static).replace("{}", url)}")
                    bmp = bitmap
                }
            })
        return bmp
    }

    private fun getLocal(context: Context, file: String) : Bitmap? {
        return BitmapFactory.decodeStream(BufferedInputStream(FileManager.readRaw(context, file)))
    }

    private fun persist(context: Context, path: String, bitmap: Bitmap) {
        try {
            context.openFileOutput(path, Context.MODE_PRIVATE).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
