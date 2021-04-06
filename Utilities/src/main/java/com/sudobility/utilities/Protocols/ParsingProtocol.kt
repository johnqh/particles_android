package com.sudobility.utilities.Protocols

import com.sudobility.utilities.extensions.array
import com.sudobility.utilities.extensions.map
import org.json.JSONArray
import org.json.JSONObject

public interface ParsingProtocol {
    fun parse(jsonObject: JSONObject) {
        val map = jsonObject.map()
        map.let {
            parse(map!!)
        }
    }

    fun parse(jsonArray: JSONArray) {
        val array = jsonArray.array()
        array.let {
            parse(array!!)
        }
    }

    fun parse(dictionary: Map<String, Any>) {
    }

    fun parse(array: List<Any>) {
    }
}
