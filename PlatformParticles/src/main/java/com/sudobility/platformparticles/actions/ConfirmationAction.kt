package com.sudobility.platformparticles.actions

import com.sudobility.routingkit.router.NavigableProtocol
import com.sudobility.routingkit.router.RoutingCompletionBlock
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.prompter.PrompterAction
import com.sudobility.utilities.prompter.PrompterProtocol


open class ConfirmationAction: NSObject(), NavigableProtocol {
    override val history: RoutingRequest? = null
    open var confirmation: PrompterProtocol? = null
    var completion: RoutingCompletionBlock? = null

    override fun navigate(request: RoutingRequest?, animated: Boolean, completion: RoutingCompletionBlock?) {
        completion?.invoke(null, false)
    }

    open fun confirm(confirmation: PrompterProtocol, actions: MutableList<PrompterAction>, completion: RoutingCompletionBlock?) {
        this.confirmation = confirmation
        var actions = actions
        actions.add(PrompterAction.cancel(selection = {
            this.complete(successful = false)
        }))
        confirmation.prompt(actions)
    }

    open fun complete(successful: Boolean) {
        confirmation = null
        completion?.invoke(null, successful)
        completion = null
    }
}
