package com.sudobility.utilities.utils.parser

import com.sudobility.utilities.extensions.MapUtils
import org.json.JSONArray
import org.json.JSONObject

open class ConditionalParser: Parser() {
    private val defaultTag = "_default"
    var conditions: Map<String, String>? = null

    override open fun asString(data: Any?) : String? =
        super.asString(conditioned(data))

    override open fun asStrings(data: Any?) : List<String>? =
        super.asStrings(conditioned(data))

    override open fun asBoolean(data: Any?) : Boolean? =
        super.asBoolean(conditioned(data))

    override open fun asDictionary(data: Any?) : Map<String, Any>? =
        super.asDictionary(conditioned(data))

    override open fun asArray(data: Any?) : List<Any>? =
        super.asArray(conditioned(data))

    override open fun conditioned(data: Any?) : Any? {
        val data = data
        if (data != null) {
            var conditions = MapUtils.merge(deviceRule(), this.conditions)
            val appInfo = AppConfiguration.info
            if (appInfo != null) {
                conditions = MapUtils.merge(appInfo, conditions)
            }
            return conditioned(data!!, conditions)
        }
        return data
    }

    private fun deviceRule() : Map<String, String> {
        return mapOf()
    }

    private fun conditioned(data: Any, conditions: Map<String, Any>?) : Any? {
        val dictionary = super.asDictionary(data)
        if (dictionary != null) {
            return conditioned(dictionary, conditions) ?: data
        }
        var string = data as? String
        val conditions = conditions
        if (string != null && string.contains("<") && string.contains(">") && conditions != null) {
            for ((key, value) in conditions) {
                val valueString = Parser.standard.asString(value)
                valueString.let {
                    string = string!!.replace("<${key}>", valueString!!)
                }
            }
            return string
        } else {
            return data
        }
    }

    fun conditioned(dictionary: JSONObject, conditions: Map<String, String>?) : Any? {
        conditions?.let {
            var narrowedResult: Any? = null
            val keys = dictionary.keys()
            loop@ for (key in keys) {
                val value = dictionary.get(key)
                val node = value as? JSONObject
                if (node != null) {
                    val ruleValue = conditions!![key]
                    if (ruleValue != null) {
                        narrowedResult = node[ruleValue]
                    } else {
                        narrowedResult = node["<null>"]
                    }
                    if (narrowedResult == null) {
                        narrowedResult = node[defaultTag]
                    }
                }
                if (narrowedResult != null) {
                    break@loop
                }
            }
            if (narrowedResult != null) {
                return conditioned(narrowedResult, conditions = conditions)
            } else {
                return dictionary
            }
        }
        return dictionary
    }
}
