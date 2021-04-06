package com.sudobility.particleskit.api.webapi

import com.sudobility.particleskit.api.ApiCompletionHandler
import com.sudobility.particleskit.api.ApiProtocol
import com.sudobility.particleskit.api.endpoint.EndpointResolverProtocol
import com.sudobility.particleskit.cache.IOReadCompletionHandler
import com.sudobility.particleskit.error.log.ErrorLogging
import com.sudobility.particleskit.recorder.ApiReplayer
import com.sudobility.routingkit.router.Router
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.bridge.DispatchQueue
import com.sudobility.utilities.bridge.NSError
import com.sudobility.utilities.error.Console
import com.sudobility.utilities.json.JSONSerialization
import com.sudobility.utilities.utils.LoadingStatus
import okhttp3.*
import java.io.IOException


open class WebApi: HttpApi, ApiProtocol {
    override var priority: Int = 10
    private var client: OkHttpClient = OkHttpClient()
    private var call: Call? = null
    private var running = false
        set(newValue) {
            val oldValue = field
            field = newValue
            if (running != oldValue) {
                if (running) {
                    LoadingStatus.shared.plus()
                } else {
                    LoadingStatus.shared.minus()
                }
            }
        }

    constructor(priority: Int = 10) : super() {
        this.priority = priority
    }

    constructor(server: String? = null, priority: Int = 10) : super(server = server) {
        this.priority = priority
    }

    constructor(endpointResolver: EndpointResolverProtocol?, priority: Int = 100) : super(
        endpointResolver = endpointResolver
    ) {
        this.priority = priority
    }


    override fun load(path: String, params: Map<String, Any>?, completion: IOReadCompletionHandler) {
        DispatchQueue.main.asyncAfter(0.01F) { this.get(path = path, params = params) { data, error  ->
            completion(this.result(data = data), this.meta(data = data), this.priority, error)
        } }
    }

    override fun get(path: String, params: Map<String, Any>?, completion: ApiCompletionHandler) {
        run(verb = HttpVerb.get, path = path, params = params, body = null, completion = completion)
    }

    override fun post(
        path: String,
        params: Map<String, Any>?,
        data: Any?,
        completion: ApiCompletionHandler
    ) {
        run(
            verb = HttpVerb.post,
            path = path,
            params = params,
            body = data,
            completion = completion
        )
    }

    override fun put(
        path: String,
        params: Map<String, Any>?,
        data: Any?,
        completion: ApiCompletionHandler
    ) {
        run(verb = HttpVerb.put, path = path, params = params, body = data, completion = completion)
    }

    override fun delete(path: String, params: Map<String, Any>?, completion: ApiCompletionHandler) {
        run(
            verb = HttpVerb.delete,
            path = path,
            params = params,
            body = null,
            completion = completion
        )
    }

    open fun run(
        verb: HttpVerb,
        path: String,
        params: Map<String, Any>?,
        body: Any?,
        completion: ApiCompletionHandler
    ) {
        val server = server
        if (server != null) {
            call?.cancel()
            val pathAndParams = url(server = server, path = path, params = params)
            if (pathAndParams.urlPath.contains("{")) {
                // unresolved params
                completion(null, null)
            } else {
                run(
                    verb = verb,
                    urlPath = pathAndParams.urlPath,
                    paramStrings = pathAndParams.paramStrings,
                    body = body,
                    completion = completion
                )
            }
        } else {
            completion(null, null)
        }
    }

    open fun run(
        verb: HttpVerb,
        urlPath: String,
        paramStrings: List<String>?,
        body: Any?,
        completion: ApiCompletionHandler
    ) {
        val data = ApiReplayer.shared?.replay(urlPath = urlPath, params = paramStrings)
        if (data != null) {
            completion(data, null)
        } else {
            val url = url(path = urlPath, params = paramStrings)
            if (url != null) {
                request(verb = verb, url = url, body = body) { request  ->
                    this.run(
                        request = request,
                        urlPath = urlPath,
                        paramStrings = paramStrings,
                        completion = completion
                    )
                }
            }
        }
    }

    open fun run(
        request: URLRequest,
        urlPath: String,
        paramStrings: List<String>?,
        completion: ApiCompletionHandler
    ) {
        client = OkHttpClient()
        call = client.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                DispatchQueue.main.async {
                    val error = NSError()
                    completion(null, error)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                DispatchQueue.main.async {
                    response.use {
                        var data: Any? = null
                        if (response.isSuccessful) {
                            data = response.body
                        }

                        if (data != null) {
                            ApiReplayer.shared?.record(
                                urlPath = urlPath,
                                params = paramStrings,
                                data = data
                            )
                            val code = response.networkResponse?.code
                            if (code != null && code == 204 || !(200 .. 299).contains(code)) {
                                val error = NSError(domain = "${className}.response.fail", code = code!!, userInfo = data as? Map<String, Any>)
                                ErrorLogging.shared?.log(error)
                                if (code == 401) {
                                    Router.shared?.navigate(RoutingRequest(
                                            path = "/action/logout",
                                            params = null
                                        ), true, null
                                    )
                                }
                                completion(data, error)
                            } else {
                                val error = NSError()
                                completion(data, error)
                            }
                        } else {
                            val error = NSError()
                            completion(data, error)
                        }
                    }
                }
            }
        })
    }

}
