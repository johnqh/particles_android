package com.sudobility.particleskit.api.webapi

import com.sudobility.utilities.kvo.NSObject
import okhttp3.Request


open class WebApiRequestHeaderInjection: NSObject, WebApiRequestInjectionProtocol {
    internal var headers: Map<String, String>? = null

    constructor(headers: Map<String, String>?) : super() {    this.headers = headers
    }

    override fun inject(request: Request.Builder, verb: HttpVerb, completion: (request: Request.Builder) -> Unit) {
        var request = request
        val headers = headers
        if (headers != null) {
            for ((key, value) in headers) {
                request = request.header(key, value)
            }
            completion(request)
        } else {
            completion(request)
        }
    }

    override fun cookies(completion: (Map<String, String>?) -> Unit) {
        completion(null)
    }
}
