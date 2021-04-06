package com.sudobility.utilities.utils.parser

import com.sudobility.utilities.extensions.array
import com.sudobility.utilities.extensions.map
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*
import kotlin.math.roundToInt


open class Parser {
    public companion object {
        public var standard: Parser = { Parser() }()
    }

    open fun conditioned(data: Any?) : Any? =
        data

    open fun asString(data: Any?) : String? {
        var temp: String? = null
        val date = data as? Date
        if (date != null) {
            temp = date.toString()
        }

        val string = data as? String
        if (string != null) {
            temp = string
        }

        if (temp == "<null>") {
            temp = null
        }
        return temp?.trim()
    }

    open fun asValidString(data: Any?) : String? {
        val string = asString(data)
        if (string != null && string != "") {
            return string
        }
        return null
    }

    open fun asStrings(data: Any?) : List<String>? {
        val strings = data as? List<String>
        if (strings != null) {
            return strings
        }

        val string = asString(data)
        if (string != null) {
            val lines = string.split(",")
            var strings = mutableListOf<String>()
            for (line in lines) {
                val trimmed = line.trim()
                if (trimmed != null) {
                    strings.add(trimmed)
                }
            }
            return strings
        }
        return null
    }

    open fun asBoolean(data: Any?) : Boolean? {
        val string = (data as? String)?.toLowerCase()
        if (string != null) {
            if (string == "y" || string == "1" || string == "true" || string == "yes" || string == "on") {
                return true
            } else if (string == "n" || string == "0" || string == "false" || string == "no" || string == "off") {
                return false
            }
        }
        val boolean = data as? Boolean
        if (boolean != null) {
            return boolean
        }

        val int = data as? Int
        if (int != null) {
            return int != 0
        }
        return null
    }

    open fun asDictionary(data: Any?) : Map<String, Any>? {
        val json = data as? JSONObject
        if (json != null) {
            return json.map()
        }
        else {
            return data as? Map<String, Any>
        }
    }

    open fun asArray(data: Any?) : List<Any>? {
        data?.let {
            if (data is JSONObject) {
                return  null
            }
            val json = data as? JSONArray
            if (json != null) {
                return json.array()
            }

            return listOf(data)
        }
        return null
    }

    open fun asInt(data: Any?) : Int? {
        val int = data as? Int
        if (int != null) {
            return int
        }

        val float = data as? Float
        if (float != null) {
            return float.roundToInt()
        }

        val double = data as? Double
        if (double != null) {
            return double.roundToInt()
        }

        val string = data as? String
        if (string != null) {
            return string.toInt()
        }

        return null
    }

    open fun asFloat(data: Any?) : Float? {
        val int = data as? Int
        if (int != null) {
            return int.toFloat()
        }

        val float = data as? Float
        if (float != null) {
            return float
        }

        val double = data as? Double
        if (double != null) {
            return double.toFloat()
        }

        val string = data as? String
        if (string != null) {
            return string.toFloat()
        }

        return null
    }

    open fun asDate(data: Any?) : Date? {
        val date = data as? Date
        if (date != null) {
            return date
        }
        return null
    }

    open fun asURL(data: Any?) : URL? {
        val string = this.asString(data)
        if (string != null) {
            return URL(string)
        }
        return null
    }
}

public interface ParsingObjectProtocol {
    fun parser(): Parser {
        return Parser.standard
    }
}