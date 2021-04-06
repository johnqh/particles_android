package com.sudobility.platformparticles.actions.permissions

import com.sudobility.routingkit.router.NavigableProtocol
import com.sudobility.routingkit.router.Router
import com.sudobility.routingkit.router.RoutingCompletionBlock
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.permissions.EPrivacyPermission
import com.sudobility.utilities.permissions.PrivacyPermission


open class PrivacyPermissionAction: NSObject(), NavigableProtocol {
    override val history: RoutingRequest? = null
    var required: Boolean = false
    val primer: String?
        get() = null
    private var completion: RoutingCompletionBlock? = null
    private var pending: PrivacyPermission? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = pending, keyPath = "authorization") { _, _, _  ->
                when (this.pending?.authorization) {
                    EPrivacyPermission.unknown -> {
                    }
                    EPrivacyPermission.notDetermined -> {
                        val primer = this.primer
                        if (primer != null) {
                            Router.shared?.navigate(
                                RoutingRequest(
                                    path = primer,
                                    params = null
                                ), true, null
                            )
                        } else {
                            this.pending?.promptToAuthorize()
                        }
                    }
                    EPrivacyPermission.restricted -> {
                        if (this.required) {
                            this.completion?.invoke(null, false)
                            this.completion = null
                            val primer = this.primer
                            if (primer != null) {
                                Router.shared?.navigate(RoutingRequest(path = primer, params = null), true, null)
                            } else {
                                this.pending?.promptWithRestriction()
                            }
                        } else {
                            this.completion?.invoke(null, false)
                            this.completion = null
                        }
                    }
                    EPrivacyPermission.authorized -> {
                        this.completion?.invoke(null, true)
                        this.completion = null
                    }
                    else -> if (this.required) {
                        this.completion?.invoke(null, false)
                        this.completion = null
                        val primer = this.primer
                        if (primer != null) {
                            val request = RoutingRequest(path = primer, params = null)
                            Router.shared?.navigate(request, true, null)
                        } else {
                            this.pending?.promptToSettings()
                        }
                    } else {
                        this.completion?.invoke(null, false)
                        this.completion = null
                    }
                }
            }
        }
    open val path: String?
        get() = null

    open fun authorization() : PrivacyPermission? =
        null

    override fun navigate(request: RoutingRequest?, animated: Boolean, completion: RoutingCompletionBlock?) {
        val authorization = authorization()
        if (request?.path == path && authorization != null) {
            this.completion = completion
            pending = authorization
        } else {
            completion?.invoke(null, false)
        }
    }
}
