package com.sudobility.particleskit.api.webapi

import com.sudobility.particleskit.api.endpoint.EndpointResolverProtocol
import com.sudobility.utilities.extensions.append
import com.sudobility.utilities.extensions.encodeUrl
import com.sudobility.utilities.extensions.joined
import com.sudobility.utilities.extensions.replacingOccurrences
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.LoadingStatus
import com.sudobility.utilities.extensions.merge
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import com.sudobility.particleskit.api.webapi.URLRequest as URLRequest

public enum class HttpVerb (val rawValue: String) {
    get("GET"), post("POST"), put("PUT"), delete("DELETE");

    companion object {
        operator fun invoke(rawValue: String) = HttpVerb.values().firstOrNull { it.rawValue == rawValue }
    }
}

open class HttpApi: NSObject {
    var endpointResolver: EndpointResolverProtocol? = null
    var server: String? = null
    var status: LoadingStatus? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (status !== oldValue) {
                oldValue?.minus()
                status?.plus()
            }
        }
    var requestInjections: List<WebApiRequestInjectionProtocol>? = null
    var responseInjections: List<WebApiResponseInjectionProtocol>? = null


    constructor() : super() {
        this.server = null
    }

    constructor(server: String?) : super() {
        this.server = server
    }

    constructor(endpointResolver: EndpointResolverProtocol?) : super() {    this.endpointResolver = endpointResolver
        server = endpointResolver?.host
    }


    fun request(verb: HttpVerb, url: URL, body: Any?, completion: (request: URLRequest) -> Unit) {
        var builder = Request.Builder().url(url.httpUrl()!!)
        val bodyString = JSONObject(body as? Map<String, Any>)?.toString()
        builder = when (verb) {
            HttpVerb.get -> builder
            HttpVerb.post -> {
                if (bodyString != null) {
                    val requestBody = RequestBody.create(
                        "application/json".toMediaTypeOrNull(), bodyString);
                    builder.post(requestBody)
                } else {
                    builder
                }
            }
            HttpVerb.put -> {
                if (bodyString != null) {
                    val requestBody = RequestBody.create(
                        "application/json".toMediaTypeOrNull(), bodyString);
                    builder.post(requestBody)
                } else {
                    builder
                }
            }
            HttpVerb.delete -> {
                builder.delete()
            }
            else -> builder
        }
//        request.cachePolicy = URLRequest.CachePolicy.reloadIgnoringLocalCacheData
        inject(builder, verb = verb, index = 0) { builder  ->
            // [weak self]
            var builder = builder
            builder = builder.header("Content-Type", "application/json")
            builder = builder.header("Accept", "application/json")
            completion(builder.build())
        }
    }

    fun inject(request: Request.Builder, verb: HttpVerb, index: Int, completion: (request: Request.Builder) -> Unit) {
        val injections = requestInjections
        if (injections != null && index < injections.size) {
            val injection = injections[index]
            injection.inject(request = request, verb = verb) { request  ->
                this.inject(request = request, verb = verb, index = index + 1, completion = completion)
            }
        } else {
            completion(request)
        }
    }

    public class UrlTuple(public val urlPath: String, public val paramStrings: List<String>?) {
    }

    open fun url(server: String, path: String, params: Map<String, Any>?) : UrlTuple {
        var urlPath = "${server}${path}"
        var paramStrings: List<String>? = null
        val paramsDictionary = params
        if (paramsDictionary != null) {
            var leftover = mutableMapOf<String , Any>()
            for ((key, value) in paramsDictionary) {
                val marker = "{${key}}"
                if (urlPath.contains(marker)) {
                    urlPath = urlPath.replacingOccurrences(of = marker, with = "${value}")
                } else {
                    leftover[key] = value
                }
            }
            if (leftover.size > 0) {
                paramStrings = mutableListOf<String>()
                for ((key, value) in leftover) {
                    // transform value into string
                    if (value is String) {
                        paramStrings?.append("${key}=${value}")
                    } else {
                        val stringValue = parser.asString(value)
                        if (stringValue != null) {
                            paramStrings?.append("${key}=${stringValue}")
                        }
                    }
                }
            }
        }
        return UrlTuple(urlPath, paramStrings)
    }

    fun url(path: String, params: List<String>?) : URL? {
        var url = path
        val params = params
        if (params != null) {
            if (params.size > 0) {
                url = path + "?" + params.joined(separator = "&")
            }
        }
        val encodedUrl = url.encodeUrl()
        if (encodedUrl != null) {
            return URL(encodedUrl)
        }
        return null
    }

    fun merge(params1: Map<String, Any>?, params2: Map<String, Any>?) : Map<String, Any> {
        var merged: MutableMap<String, Any> = mutableMapOf()
        val params1 = params1
        if (params1 != null) {
            merged.merge(params1) { _, second  ->
                second
            }
        }
        val params2 = params2
        if (params2 != null) {
            merged.merge(params2) { _, second  ->
                second
            }
        }
        return merged
    }

    open fun result(data: Any?) : Any? =
        data

    open fun meta(data: Any?) : Any? =
        null

}
