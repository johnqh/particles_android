package com.sudobility.routingkit.action

import com.sudobility.routingkit.router.NavigableProtocol
import com.sudobility.routingkit.router.RoutingCompletionBlock
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.kvo.NSObject

open class RoutingAction() : NSObject(), NavigableProtocol {
    override val history: RoutingRequest? = null

    open var completion: RoutingCompletionBlock? = null

    override fun navigate(request: RoutingRequest?, animated: Boolean, completion: RoutingCompletionBlock?) {
        completion?.invoke(null, false)
    }

    open fun complete(successful: Boolean) {
        completion?.invoke(null, successful)
        completion = null
    }
}
