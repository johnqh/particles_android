package com.sudobility.utilities.extensions

import com.sudobility.utilities.files.FileUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

public object JSONTransform {
    public fun transform(obj: Any): Any {
        val dictionary = obj as? JSONObject
        if (dictionary != null) {
            return transform(dictionary!!)
        }
        val array = obj as? JSONArray
        if (array != null) {
            return transform(array!!)
        }
        return obj
    }

    public fun transform(dictionary: JSONObject): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = dictionary.keys()
        for (key in keys) {
            val value = dictionary.get(key)
            value?.let {
                val transformed = transform(value!!)
                map[key] = transformed
            }
        }
        return map
    }

    public fun transform(array: JSONArray): List<Any> {
        val list = mutableListOf<Any>()
        for (i in 0 until array.length()) {
            val value = array[i]
            value?.let {
                val transformed = transform(value!!)
                list.add(transformed)
            }
        }
        return list
    }
}

public fun JSONArray.array(): List<Any>? {
    return JSONTransform.transform(this)
}

public fun JSONObject.map(): Map<String, Any>?{
    return JSONTransform.transform(this)
}