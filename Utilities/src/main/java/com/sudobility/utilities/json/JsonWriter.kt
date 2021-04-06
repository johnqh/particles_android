package com.sudobility.utilities.json

import android.R.attr.data
import android.content.Context
import android.util.Log
import com.sudobility.utilities.app.UtilsApplication
import org.w3c.dom.Text
import java.io.IOException
import java.io.OutputStreamWriter


public object JsonWriter {
    fun write(obj: Any?, to: String?) {
        val string = JSONSerialization.data(obj)
        if (string != null && to != null) {
            TextFileIO.write(to, string)
        }
    }
}
