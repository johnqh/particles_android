package com.sudobility.particlescommonmodels.actions

import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.particleskit.interactor.protocol.ActionProtocol
import com.sudobility.routingkit.router.RoutingRequest

open class SimpleAction: DictionaryEntity(), ActionProtocol {
    override val title: String?
        get() = parser.asString(data?.get("title"))
    override val subtitle: String?
        get() = parser.asString(data?.get("subtitle"))
    override val detail: String?
        get() = parser.asString(data?.get("detail"))
    override val image: String?
        get() = parser.asString(data?.get("image"))
    override val routing: RoutingRequest?
        get() {
            val routing = parser.asDictionary(data?.get("routing"))
            val path = parser.asString(routing?.get("path"))
            if (routing != null && path != null) {
                return RoutingRequest(path = path, params = parser.asDictionary(routing?.get("params")) as? Map<String, String>?)
            }
            return null
        }
    override val detailRouting: RoutingRequest?
        get() {
            val routing = parser.asDictionary(data?.get("detailRouting"))
            val path = parser.asString(routing?.get("path"))
            if (routing != null && path != null) {
                return RoutingRequest(path = path, params = parser.asDictionary(routing?.get("params")) as? Map<String, String>?)
            }
            return null
        }
}
