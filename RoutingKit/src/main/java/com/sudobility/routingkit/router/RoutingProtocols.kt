package com.sudobility.routingkit.router

import java.net.URL

public enum class RoutingPresentation (val rawValue: Int) {
    root(0), show(1), detail(2), prompt(3), callout(4), half(5), float(6), embed(7), drawer(8);

    companion object {
        operator fun invoke(rawValue: Int) = RoutingPresentation.values().firstOrNull { it.rawValue == rawValue }
    }
}


typealias RoutingCompletionBlock = (Any?, Boolean) -> Unit

public interface RoutingOriginatorProtocol {
    fun routingRequest() : RoutingRequest? {
        return null
    }
    fun identifierParams() : Map<String, Any>? {
        return null
    }
}

interface RouterProtocol {
    var disabled: Boolean
    fun navigate(request: RoutingRequest, presentation: RoutingPresentation?, animated: Boolean, completion: RoutingCompletionBlock?)
    fun navigate(url: URL?, completion: RoutingCompletionBlock?)


    fun navigate(request: RoutingRequest, animated: Boolean, completion: RoutingCompletionBlock?) {
        navigate(request, presentation = null, animated, completion)
    }

    fun navigate(originator: RoutingOriginatorProtocol, animated: Boolean, completion: RoutingCompletionBlock?) {
        navigate(originator, presentation = null, animated, completion)
    }

    fun navigate(originator: RoutingOriginatorProtocol, presentation: RoutingPresentation? = null, animated: Boolean, completion: RoutingCompletionBlock?) {
        val routingRequest = originator.routingRequest()
        if (routingRequest != null) {
            navigate(routingRequest, presentation, animated, completion)
        }
    }
}


open class Router {
    companion object {
        var shared: RouterProtocol? = null
    }
}

interface NavigableProtocol {
    fun navigate(request: RoutingRequest?, animated: Boolean, completion: RoutingCompletionBlock?)
    val history: RoutingRequest?
}
