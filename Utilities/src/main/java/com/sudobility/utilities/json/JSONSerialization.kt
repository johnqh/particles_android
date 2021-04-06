package com.sudobility.utilities.json

import org.json.JSONArray
import org.json.JSONObject

public object JSONSerialization {
    public fun data(jsonObject: Any?): String? {
        val map = jsonObject as? Map<String, String>
        map?.let {
            return JSONObject(map).toString()
        }

        val list = jsonObject as? List<Any>
        list?.let {
            return JSONArray(list).toString()
        }
        return null
    }
}