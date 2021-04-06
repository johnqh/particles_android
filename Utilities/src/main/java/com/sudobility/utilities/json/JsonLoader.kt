package com.sudobility.utilities.json

import android.content.Context
import org.json.JSONObject

public object JsonLoader{
    fun load(file: String?): JSONObject? {
        val text = TextFileIO.asset(file)
        text?.let {
            return JSONObject(text)
        }
        return null
    }
}