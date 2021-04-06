package com.sudobility.routingkit.router

import android.net.Uri
import com.sudobility.utilities.extensions.params
import java.net.URL


public class RoutingRequest {
    var scheme: String? = null
    var host: String? = null
    var path: String? = null
    var params: Map<String, String>? = null
    var presentation: RoutingPresentation? = null
    val url: URL?
        get() {
            scheme.let {
                var builder = Uri.Builder().scheme(scheme)
                host.let {
                    builder = builder.authority(host)
                }
                path.let {
                    builder = builder.path(path)
                }
                params.let {
                    for ((key, value) in params!!) {
                        builder = builder.appendQueryParameter(key, value)
                    }
                }

                return URL(builder.build().toString());
            }
            return null
        }

    constructor(
        scheme: String? = null,
        host: String? = null,
        path: String,
        params: Map<String, String>? = null
    ) : super() {
        this.scheme = scheme
        this.host = host
        this.path = path
        this.params = params
    }

    constructor(url: String?) : super() {
        url.let {
            val uri = Uri.parse(url);
            scheme = uri.scheme
            host = uri.host
            path = uri.path
            params = uri.params()
        }
    }
}
