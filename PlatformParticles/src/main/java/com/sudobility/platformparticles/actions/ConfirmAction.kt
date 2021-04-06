package com.sudobility.platformparticles.actions

import com.sudobility.routingkit.router.NavigableProtocol
import com.sudobility.routingkit.router.RoutingCompletionBlock
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.prompter.*


open class ConfirmAction: NSObject(), NavigableProtocol {
    override val history: RoutingRequest? = null
    open var confirmation: PrompterProtocol? = null
    var completion: RoutingCompletionBlock? = null

    override fun navigate(request: RoutingRequest?, animated: Boolean, completion: RoutingCompletionBlock?) {
        if (request?.path == "/confirm") {
            val title = parser.asString(request?.params?.get("title"))
            val text = parser.asString(request?.params?.get("text"))
            val confirm = parser.asString(request?.params?.get("confirm"))
            this.confirm(title = title, text = text, confirm = confirm, completion = completion)
        } else {
            completion?.invoke(null, false)
        }
    }

    open fun confirm(title: String?, text: String?, confirm: String?, completion: RoutingCompletionBlock?) {
        val prompter = PrompterFactory.shared?.prompter()
        if (text != null || title != null && prompter != null) {
            prompter?.set(title = title, message = text, style = PrompterStyle.error)
            this.completion = completion
            var actions = mutableListOf<PrompterAction>()
            actions.add(PrompterAction(title = confirm ?: "Yes", style = PrompterActionStyle.destructive, enabled = true, selection = {
                this.complete(successful = true)
            }))
            actions.add(PrompterAction(title = "Cancel", style = PrompterActionStyle.cancel, enabled = true, selection = {
                this.complete(successful = false)
            }))
            prompter?.prompt(actions)
            confirmation = prompter
        } else {
            completion?.invoke(null, false)
        }
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
