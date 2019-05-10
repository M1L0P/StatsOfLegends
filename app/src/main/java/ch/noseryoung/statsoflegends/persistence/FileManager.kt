package ch.noseryoung.statsoflegends.persistence

import android.content.Context


object FileManager {

    fun write(context: Context, file: String, content: String) {
        context.openFileOutput(file, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }

    fun read(context: Context, file: String) : String? {
        return context.openFileInput(file).bufferedReader().readLines().joinToString("\n")
    }
}