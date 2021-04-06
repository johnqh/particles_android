package com.sudobility.platformjedio.action

import com.sudobility.jediokit.field.FieldInputDefinition
import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.particleskit.interactor.localdata.LocalEntityCacheInteractor
import com.sudobility.platformjedio.field.MapFieldLoader
import com.sudobility.platformparticles.actions.ConfirmationAction
import com.sudobility.routingkit.router.RoutingCompletionBlock
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.prompter.PrompterAction
import com.sudobility.utilities.prompter.PrompterActionStyle
import com.sudobility.utilities.prompter.PrompterFactory
import com.sudobility.utilities.prompter.PrompterStyle
import com.sudobility.utilities.tracker.Tracking
import java.net.URL


open class MapAction: ConfirmationAction() {
    open var fieldLoader: MapFieldLoader? = null
    open var mapCache: LocalEntityCacheInteractor? = null
    private val inputDefinition: FieldInputDefinition?
        get() = fieldLoader?.definitionGroups?.firstOrNull()?.definitions?.firstOrNull() as? FieldInputDefinition
    var params: Map<String, Any>? = null
    private var addressParam: String? = null
    private var coordinateParam: String? = null
    private var backUrlParam: String? = null

    open var mapUrl: String?
        get() = parser.asString((mapCache?.entity as? DictionaryEntity)?.data?.get("map"))
        set(value) {
            if (value != null) {
                (mapCache?.entity as? DictionaryEntity)?.force?.data?.set("map", value)
            } else {
                (mapCache?.entity as? DictionaryEntity)?.force?.data?.remove("map")
            }
            mapCache?.save()
            navigate()
        }

    open override fun navigate(request: RoutingRequest?, animated: Boolean, completion: RoutingCompletionBlock?) {
        if (request?.path == "/action/map") {
            Tracking.shared?.path(request?.path, data = null)
            val address = parser.asString(request?.params?.get("address"))
            val lat = parser.asFloat(request?.params?.get("lat"))
            val lng = parser.asFloat(request?.params?.get("lng"))
            if (address != null || lat != null || lng != null) {
                params = request?.params
                loadMapUrl(completion = completion)
            } else {
                completion?.invoke(null, false)
            }
        } else {
            completion?.invoke(null, false)
        }
    }

    private fun loadMapUrl(completion: RoutingCompletionBlock?) {
        prompt(mapUrl = mapUrl, completion = completion)
    }

    private fun prompt(mapUrl: String?, completion: RoutingCompletionBlock?) {
        val definition = inputDefinition
        val key = definition?.field?.get("field") as? String
        val options = definition?.options
        if (definition != null && key != null && options != null) {
            val option = options.firstOrNull()
            val value = option?.get("value") as? String
            if (options.size == 1 && option != null && value != null) {
                set(mapUrl = value)
            } else if (options.size > 1) {
                val prompter = PrompterFactory.shared?.prompter()
                if (prompter != null) {
                    prompter.set(title = "Select which routing app you would like to use", message = null, style = PrompterStyle.selection)
                    var promptActions = mutableListOf<PrompterAction>()
                    var found = false
                    for (option in options) {
                        val text = option["text"] as? String
                        val value = option["value"] as? String
                        if (text != null && value != null) {
                            if (value == mapUrl) {
                                found = true
                            } else {
                                val action = PrompterAction(title = text, style = PrompterActionStyle.normal, enabled = true) {
                                    var data: MutableMap<String, Any> = this.mapCache?.dictionaryEntity?.data?.toMutableMap() ?: mutableMapOf()
                                    data[key] = value
                                    this.mapCache?.dictionaryEntity?.data = data
                                    this.set(mapUrl = value)
                                }
                                promptActions.add(action)
                            }
                        }
                    }
                    if (found) {
                        set(mapUrl = mapUrl)
                    } else {
                        confirm(confirmation = prompter, actions = promptActions, completion = completion)
                    }
                }
            }
        }
    }

    open fun set(mapUrl: String?) {
        this.mapUrl = mapUrl
    }

    private fun option(mapUrl: String?) : Map<String, Any>? {
        val definition = inputDefinition
        val options = definition?.options
        val mapUrl = mapUrl
        if (definition != null && options != null && mapUrl != null) {
            return options.firstOrNull() { option  ->
                val value = parser.asString(option["value"])
                value == mapUrl
            }
        }
        return null
    }

    open fun navigate() {
        reallyNavigate()
    }

    open fun reallyNavigate() {
//        val mapUrl = mapUrl
//        var url = URL(string = mapUrl)
//        var urlComponents = URLComponents(url = url, resolvingAgainstBaseURL = false)
//        if (mapUrl != null && url != null && urlComponents != null) {
//            val option = this.option(mapUrl = mapUrl)
//            val link = option?["link"] as? String
//            if (link != null) {
//                val linkUrl = URL(string = link)
//                val linkUrlComponents = URLComponents(url = linkUrl, resolvingAgainstBaseURL = false)
//                if (linkUrl != null && linkUrlComponents != null) {
//                    url = linkUrl
//                    urlComponents = linkUrlComponents
//                }
//            }
//            // "link":"comgooglemapsurl://maps.google.com/maps",
//            val addressParam = option?["address"] as? String ?: "address"
//            val coordinateParam = option?["coordinate"] as? String ?: "ll"
//            val appParam = (option?["app"] as? String)?.replacingOccurrences(of = " ", with = "+")
//            val backUrlParam = option?["backUrl"] as? String
//            val drivingParam = option?["driving"] as? String
//            var params: Map<String, String> = mapOf()
//            val address = parser.asString(this.params?["address"])
//            if (address != null) {
//                params[addressParam] = address
//            }
//            val lat = parser.asNumber(this.params?["lat"])
//            val lng = parser.asNumber(this.params?["lng"])
//            if (lat != null && lng != null) {
//                params[coordinateParam] = "${lat},${lng}"
//            }
//            val driving = parser.asString(this.params?["driving"])
//            val drivingParam = drivingParam
//            if (driving != null && drivingParam != null) {
//                params[driving] = drivingParam
//            }
//            val additional = option?["additional"] as? Map<String, String>
//            if (additional != null) {
//                for (arg0 in additional) {
//                    val (key, value) = arg0
//                    params[key] = value
//                }
//            }
//            val app = parser.asString(this.params?["app"])
//            val appParam = appParam
//            if (app != null && appParam != null) {
//                params[app] = appParam
//            }
//            val backUrl = parser.asString(this.params?["backUrl"])
//            val backUrlParam = backUrlParam
//            if (backUrl != null && backUrlParam != null) {
//                params[backUrl] = backUrlParam
//            }
//            urlComponents.queryItems = params.map({ arg0  ->
//                val (key, value) = arg0
//                URLQueryItem(name = key, value = parser.asString(value))
//            })
//            val finalUrl = urlComponents.url
//            if (finalUrl != null) {
//                URLHandler.shared?.open(finalUrl) { _  ->
//                    this.complete(successful = true)
//                }
//            } else {
//                complete(successful = false)
//            }
//        } else {
//            complete(successful = false)
//        }
    }
}
