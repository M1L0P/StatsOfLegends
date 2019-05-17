package ch.noseryoung.statsoflegends.persistence

import android.content.Context
import java.io.FileInputStream

/**
 * File manager class to access local storage files
 */
object FileManager {

    /**
     * Write to a file in the local storage
     *
     * @param context Context to use to access storage
     * @param file Path to the new file
     * @param content Content data of the file to write
     */
    fun write(context: Context, file: String, content: String) {
        context.openFileOutput(file, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }

    /**
     * Read from a file in local storage
     *
     * @param context Context to use to access file
     * @param file Path to file to read
     * @return Content of the file, null if the file was not found
     */
    fun read(context: Context, file: String) : String? {
        return context.openFileInput(file).bufferedReader().readLines().joinToString("\n")
    }

    /**
     * @see FileManager.read
     *
     * @param context Context to use to access file
     * @param file Path to file to read
     * @return Open file stream to the file
     */
    fun readRaw(context: Context, file: String): FileInputStream? {
        return context.openFileInput(file)
    }
}