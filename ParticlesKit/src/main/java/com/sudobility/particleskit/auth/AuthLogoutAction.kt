package com.sudobility.particleskit.auth
import com.sudobility.routingkit.router.NavigableProtocol
import com.sudobility.routingkit.router.RoutingCompletionBlock
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.auth.AuthService
import com.sudobility.utilities.kvo.NSObject

open class AuthLogoutAction: NSObject(), NavigableProtocol {
    override val history: RoutingRequest? = null
    override fun navigate(request: RoutingRequest?, animated: Boolean, completion: RoutingCompletionBlock?) {
        if (request?.path == "/action/logout" || request?.path == "/logout") {
            val provider = AuthService.shared.provider
            if (provider != null) {
                val token = provider.token
                if (token != null) {
                    provider.logout(token = token) { successful  ->
                        // [weak self]
                        completion?.invoke(null, successful)
                    }
                } else {
                    completion?.invoke(null, true)
                }
            } else {
                completion?.invoke(null, true)
            }
        } else {
            completion?.invoke(null, false)
        }
    }
}
