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

    fun get(id: String, imageType: ImageType, context: Context): Bitmap? {
        if(id.isEmpty()) return null

        val localFile = generateFile(id, imageType)

        return if(localFile.exists())
            getLocal(localFile, context)
        else {
            val bitmap = getOnline(id, imageType, context)
            if(bitmap != null) persist(localFile, bitmap, context)
            bitmap
        }
    }

    /**
     * Get an image online via the picasso library
     *
     * @param context Context from which the function gets called
     * @param id ID of the image to get
     * @param urlID ID of the url string
     * @return Bitmap image, if image was not found null
     */
    private fun getOnline(id: String, imageType: ImageType, context: Context) : Bitmap? {
        var bmp: Bitmap? = null
        Picasso.with(context)
            .load(context.getString(imageType.urlId).replace("{}", id))
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                override fun onBitmapFailed(errorDrawable: Drawable?) {
                    Log.e("MilooliM", "Failed to get bitmap at ${context.getString(imageType.urlId).replace("{}", id)}")
                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    bmp = bitmap
                }
            })
        return bmp
    }
    
    private fun generateFile(id: String, imageType: ImageType): File {
        return File("${imageType.prefix}_$id.png")
    }

    /**
     * Load the image from local storage
     *
     * @param context Context from which the function gets called
     * @param file File path to the image
     * @return Bitmap of image, null if the splash image was not found
     */
    private fun getLocal(path: File, context: Context) : Bitmap? {
        return BitmapFactory.decodeStream(
            BufferedInputStream(FileManager(context).readRaw(path.absolutePath)))
    }

    /**
     * Persist an image on local storage
     *
     * @param path Path to the local image
     * @param bitmap Bitmap data of the image
     * @param context Context from which the function gets called
     */
    private fun persist(path: File, bitmap: Bitmap, context: Context) {
        try {
            Log.d("MilooliM", "Save image to $path")
            context.openFileOutput(path.path, Context.MODE_PRIVATE).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            Log.e("MilooliM", "Failed to persist the image $path")
        }
    }
}

enum class ImageType(val prefix: String, val urlId: Int) {
    ICON("icon", R.string.url_static_profile),
    SPLASH("splash", R.string.url_static_splash),
    CHAMPION("champion", R.string.url_static_champ),
    ITEM("item", R.string.url_static_item),
    PERK("perk", R.string.url_static_perk),
    SPELL("spell", R.string.url_static_spell)
}
