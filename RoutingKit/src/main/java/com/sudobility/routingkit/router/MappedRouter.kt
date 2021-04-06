package com.sudobility.routingkit.router

import android.net.Uri
import com.sudobility.utilities.Protocols.ParsingProtocol
import com.sudobility.utilities.error.Console
import com.sudobility.utilities.extensions.MapUtils
import com.sudobility.utilities.extensions.map
import com.sudobility.utilities.extensions.params
import com.sudobility.utilities.json.JsonLoader
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.url.URLHandler
import com.sudobility.utilities.utils.parser.Parser
import org.json.JSONObject
import java.net.URL

class RoutingMap(destination: String) : NSObject(), ParsingProtocol {
    var dependencies: List<RoutingRequest>? = null
    var presentation: RoutingPresentation? = null
    override open val parser: Parser
        get() = MappedRouter.parserOverwrite ?: super.parser

    override fun parse(dictionary: Map<String, Any>) {
        dependencies = parseDependencies(dictionary)
        when (parser.asString(dictionary["presentation"])) {
            "root" -> presentation = RoutingPresentation.root
            "drawer" -> presentation = RoutingPresentation.drawer
            "show" -> presentation = RoutingPresentation.show
            "detail" -> presentation = RoutingPresentation.detail
            "prompt" -> presentation = RoutingPresentation.prompt
            "callout" -> presentation = RoutingPresentation.callout
            "half" -> presentation = RoutingPresentation.half
            "float" -> presentation = RoutingPresentation.float
            "embed" -> presentation = RoutingPresentation.embed
            else -> presentation = null
        }
    }

    fun parseDependencies(dictionary: Map<String, Any>) : List<RoutingRequest>? {
        val dependencies = parser.asArray(dictionary["dependencies"])
        if (dependencies != null) {
            var requests = mutableListOf<RoutingRequest>()
            for (i in 0 until dependencies!!.count()) {
                val dependency = dependencies[i]
                val data = parser.asDictionary(dependency)
                var path = parser.asString(data?.get("path"))
                if (data != null && path != null) {
                    val params = parser.asDictionary(data["params"])
                    val request = RoutingRequest(
                        path = path,
                        params = params as? Map<String, String>
                    )
                    requests.add(request)
                } else {
                    path = parser.asString(dependency)
                }
                if (path != null) {
                    val request = RoutingRequest(path = path)
                    requests.add(request)
                } else {
                    Console.shared.log("Error: parsing route dependencies")
                }
            }
            return requests
        }
        return null
    }
}

open class MappedRouter: NSObject, RouterProtocol, ParsingProtocol {
    companion object {
        var parserOverwrite: Parser? = null
    }

    override open val parser: Parser
        get() = MappedRouter.parserOverwrite ?: super.parser
    override var disabled: Boolean = false
    var defaults: Map<String, Any>? = null
    var aliases: Map<String, Any>? = null
    var maps: Map<String, Map<String, Map<String, RoutingMap>>>? = null

    // ["http":["www.domain.com": ["/": "Home]]]
//    public var shared: [String: RoutingMap]
    constructor(file: String) : super() {
        val json = JsonLoader.load("routing.json")
        json?.let {
            parse(json!!)
        }
    }

    override fun parse(dictionary: JSONObject) {
        val shared: Map<String, Any>? = null
        val defaultData = (dictionary.get("defaults") as? JSONObject)?.map()
        if (defaultData != null) {
            if (defaults == null) {
                defaults = defaultData
            } else {
                defaults = MapUtils.merge(defaults, defaultData)
            }
        }
        val aliasesData = (dictionary.get("aliases") as? JSONObject)?.map()
        if (aliasesData != null) {
            if (aliases == null) {
                aliases = aliasesData
            } else {
                defaults = MapUtils.merge(aliases, aliasesData)
            }
        }
        val schemaMaps = parseMaps((dictionary.get("mapping") as? JSONObject)?.map(), shared)
        if (schemaMaps != null) {
            if (maps == null) {
                maps = schemaMaps
            } else {
                defaults = MapUtils.merge(maps, schemaMaps)
            }
        }
    }

    private fun parseMaps(dictionary: Map<String, Any>?, shared: Map<String, Any>?) : Map<String, Map<String, Map<String, RoutingMap>>>? {
        val dictionary = dictionary
        if (dictionary != null) {
            var schemeMaps = mutableMapOf<String, Map<String, Map<String, RoutingMap>>>()
            for ((scheme, value) in dictionary) {
                val dictionary = value as? Map<String, Any>
                if (dictionary != null) {
                    var hostMaps = mutableMapOf<String, Map<String, RoutingMap>>()
                    for ((host, value) in dictionary) {
                        val dictionary = merge(host, value as? Map<String, Any>, shared)
                        if (dictionary != null) {
                            var maps = mutableMapOf<String, RoutingMap>()
                            for ((key, value) in dictionary) {
                                val dictionary = parser.asDictionary(value)
                                val destination = parser.asString(dictionary?.get("destination"))
                                if (dictionary != null && destination != null) {
                                    val routing = map(destination = destination)
                                    routing.parse(dictionary = dictionary)
                                    maps[key] = routing
                                }
                            }
                            hostMaps[host] = maps
                        }
                    }
                    schemeMaps[scheme] = hostMaps
                }
            }
            return schemeMaps
        }
        return null
    }

    private fun merge(host: String, destination: Map<String, Any>?, shared: Map<String, Any>?) : Map<String, Any>? {
        if (host == "*") {
            return destination
        } else {
            val destination = destination
            if (destination != null) {
                val shared = shared
                if (shared != null) {
                    return MapUtils.merge(destination, shared)
                } else {
                    return destination
                }
            } else {
                return shared
            }
        }
    }

    open fun map(destination: String) : RoutingMap =
        RoutingMap(destination = destination)

    override fun navigate(
        request: RoutingRequest,
        presentation: RoutingPresentation?,
        animated: Boolean,
        completion: RoutingCompletionBlock?
    ) {
        if (disabled) {
            completion?.invoke(null, false)
        } else {
            val transformed = transform(request = request)
            val map = this.map(transformed)
            if (map != null) {
                backtrack(request = transformed, animated = animated) { _, completed  ->
                    if (completed) {
                        completion?.invoke(null, true)
                    } else {
                        this.route(map, request = transformed, completion = { _, successful ->
                            if (successful) {
                                this.navigate(
                                    map,
                                    transformed,
                                    presentation ?: transformed.presentation,
                                    animated,
                                    completion
                                )
                            } else {
                                completion?.invoke(null, false)
                            }
                        })
                    }
                }
            } else {
                val url = request.url
                if (url != null) {
                    URLHandler.shared?.open(url, completion = { successful ->
                        completion?.invoke(null, successful)
                    })
                } else {
                    completion?.invoke(null, false)
                }
            }
        }
    }

    open fun transform(request: RoutingRequest) : RoutingRequest {
        val path = request.path
        if (path != null) {
            val transformed = RoutingRequest(
                transform(
                    request.scheme,
                    defaults?.get("scheme") as? String
                ),
                transform(request.host, defaults?.get("host") as? String),
                path.trim() ?: "/",
                request.params
            )
            transformed.presentation = request.presentation
            return transformed
        }
        return request
    }

    open fun transform(string: String?, fallback: String?) : String? {
        val string = string
        if (string != null) {
            return (this.aliases?.get(string) as? String) ?: string
        } else {
            return fallback
        }
    }

    open fun map(request: RoutingRequest) : RoutingMap? {
        var scheme: String? = request.scheme ?: (defaults?.get("scheme") as? String)
        {
            val input = scheme
            val alias = aliases?.get(input) as? String
            if (input != null && alias != null) {
                scheme = alias
            }
        }
        var host: String? = request.host ?: (defaults?.get("host") as? String)
        {
            val input = host
            val alias = aliases?.get(input) as? String
            if (input != null && alias != null) {
                host = alias
            }
        }
        if (scheme != null && host != null) {
            val path = request.path
            if (path != null) {
                return maps?.get(scheme)?.get(host)?.get(path) ?: maps?.get(scheme)?.get(host)?.get(
                    "*"
                ) ?: maps?.get(scheme)?.get("*")?.get("*")
            } else {
                return maps?.get(scheme)?.get(host)?.get("*") ?: maps?.get(scheme)?.get("*")?.get("*")
            }
        }
        return null
    }

    open fun backtrack(
        request: RoutingRequest,
        animated: Boolean,
        completion: RoutingCompletionBlock?
    ) {
        completion?.invoke(null, false)
    }

    open fun route(
        map: RoutingMap,
        index: Int = 0,
        request: RoutingRequest,
        completion: RoutingCompletionBlock?
    ) {
        val dependency = map.dependencies?.get(index)
        val path = dependency?.path
        if (index < map.dependencies?.size ?: 0 && dependency != null && path != null) {
            var params = dependency.params ?: mapOf<String, Any>()
            val others = request.params
            if (others != null) {
                params = MapUtils.merge(params, others)!!
            }
            val request = RoutingRequest(path = path, params = params as? Map<String, String>)
            navigate(request, null, false) { _, successful  ->
                if (successful) {
                    this.route(map, index + 1, request, completion)
                } else {
                    completion?.invoke(null, false)
                }
            }
        } else {
            completion?.invoke(null, true)
        }
    }

    open fun navigate(
        map: RoutingMap,
        request: RoutingRequest,
        presentation: RoutingPresentation?,
        animated: Boolean,
        completion: RoutingCompletionBlock?
    ) {
        completion?.invoke(null, false)
    }

    override fun navigate(url: URL?, completion: RoutingCompletionBlock?) {
        val uri: Uri = Uri.parse(url.toString())
        // Sample URL app://go.to/path...
        val path = uri?.path
        if (path != null) {
            navigate(
                RoutingRequest(
                    scheme = uri?.scheme,
                    host = uri?.host,
                    path = path,
                    params = uri?.params()
                ), presentation = null, animated = true, completion = completion
            )
        }
    }
}
