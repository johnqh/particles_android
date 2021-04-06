package com.sudobility.particleskit.entity.common

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.presenter.`object`.SelectableProtocol
import com.sudobility.particleskit.presenter.protocol.XibProviderProtocol
import com.sudobility.routingkit.router.RoutingOriginatorProtocol
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.Protocols.ParsingProtocol
import com.sudobility.utilities.json.JsonLoader
import com.sudobility.utilities.kvo.NSObject


public open class XibAction: NSObject(), XibProviderProtocol, ModelObjectProtocol, SelectableProtocol,
    ParsingProtocol, RoutingOriginatorProtocol {
    /*
    companion object {
        fun load(file: String) : List<XibAction>? {
            var actions: List<XibAction>? = null
            val bundles = Bundle.particles
            for (bundle in bundles) {
                actions = load(file = file, bundle = bundle)
                if (actions != null) {
                    break
                }
            }
            return actions
        }

        fun load(file: String, bundle: Bundle) : List<XibAction>? {
            val data = JsonLoader.load(bundle = bundle, fileName = file) as? List<Map<String, Any>>
            if (data != null) {
                var objects = listOf<XibAction>()
                for (item in data) {
                    val object = XibAction()
                    object.parse(dictionary = item)
                    objects.append(object)
                }
                return if (objects.size > 0) objects else null
            }
            return null
        }
    }
    */

    override var isSelected: Boolean
        get() = value("isSelected") as? Boolean ?: false
        set(value) = set(value, "isSelected")

    override var xib: String?
        get() = value("xib") as? String
        set(value) = set(value, "xib")

    var title: String?
        get() = value("title") as? String
        set(value) = set(value, "title")

    var text: String?
        get() = value("text") as? String
        set(value) = set(value, "text")

    var image: String?
        get() = value("image") as? String
        set(value) = set(value, "image")

    var color: String?
        get() = value("color") as? String
        set(value) = set(value, "color")

    var request: RoutingRequest?
        get() = value("request") as? RoutingRequest
        set(value) = set(value, "request")

    override fun routingRequest() : RoutingRequest? =
        request

    override fun parse(dictionary: Map<String, Any>) {
        xib = parser.asString(dictionary["xib"])
        title = parser.asString(dictionary["title"])
        text = parser.asString(dictionary["text"])
        image = parser.asString(dictionary["image"])
        color = parser.asString(dictionary["color"])
        val url = parser.asString(dictionary["url"])
        if (url != null) {
            request = RoutingRequest(url = url)
        }
    }
}
