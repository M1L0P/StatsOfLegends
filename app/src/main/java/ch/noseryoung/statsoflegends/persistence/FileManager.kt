package ch.noseryoung.statsoflegends.persistence

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileReader
import java.util.stream.Collectors


open class FileManager(val context: Context) {

    fun write(file: String, content: String) {
        context.openFileOutput(file, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }

    fun read(file: String) : String? {
        val openFileInput = context.openFileInput(file)
        val bufferedReader = openFileInput.bufferedReader()
        val readLines = bufferedReader.readLines()
        val joinToString = readLines.joinToString("\n")
        return joinToString
    }


    fun readRaw(file: String): FileInputStream? {
        return context.openFileInput(file)
    }
}