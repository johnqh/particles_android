package com.sudobility.particleskit.api.webapi

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

public val Request.allHTTPHeaderFields: Map<String, String>?
    get() {
        val headers = this.headers
        if (headers != null) {
            val pairs = mutableMapOf<String, String>()
            headers.forEach { pair ->
                val (key, value) = pair
                pairs[key] = value
            }
            return pairs
        }
        return null
    }

public fun Request.with(headers: Map<String, String>?): Request {
    if (headers != null) {
        var builder = this.newBuilder()
        for (args0 in headers) {
            val (key, value) = args0
            builder = builder.header(key, value)
        }
        return builder.build()
    } else {
        return this
    }
}

public fun URL.httpUrl(): HttpUrl? {
    return this.toString().toHttpUrlOrNull()
}