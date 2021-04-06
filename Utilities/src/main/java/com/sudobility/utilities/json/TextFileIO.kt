package com.sudobility.utilities.json

import android.content.Context
import com.sudobility.utilities.app.UtilsApplication
import java.io.FileOutputStream
import java.io.InputStream


public object TextFileIO{
    public fun asset(fileName: String?): String? {
        if (fileName != null) {
            val app = UtilsApplication.shared()
            var text: String? = null
            try {
                val inputStream: InputStream = app.assets.open(fileName)
                text = inputStream.bufferedReader().use { it.readText() }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }
            return text
        }
        return null
    }

    public fun read(fileName: String?): String? {
        if (fileName != null) {
            val app = UtilsApplication.shared()

            var text: String? = null
            try {
                val inputStream: InputStream = app.openFileInput(fileName)
                text = inputStream.bufferedReader().use { it.readText() }
            } catch (ex: Exception) {
                ex.printStackTrace()
                return null
            }
            return text
        }
        return null
    }

    public fun write(fileName: String?, data: String?) {
        if (fileName != null && data != null) {
            val app = UtilsApplication.shared()
            val stream = app.openFileOutput(fileName, Context.MODE_PRIVATE)
            try {
                stream.write(data.toByteArray())
            } finally {
                stream.close()
            }
        }
    }
}