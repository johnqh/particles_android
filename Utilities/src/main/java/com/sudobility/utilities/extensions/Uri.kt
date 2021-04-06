package com.sudobility.utilities.extensions

import android.net.Uri
import org.json.JSONArray

public fun Uri.params(): Map<String, String>? {
    val keys = this.queryParameterNames
    val map = mutableMapOf<String, String>()
    for (key in keys) {
        val value = this.getQueryParameter(key)
        value?.let {
            map[key] = value
        }
    }
    return map
}