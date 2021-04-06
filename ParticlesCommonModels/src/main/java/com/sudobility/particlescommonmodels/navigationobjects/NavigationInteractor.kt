package com.sudobility.particlescommonmodels.navigationobjects

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.list.ListInteractor
import com.sudobility.particleskit.interactor.protocol.InteractorProtocol
import com.sudobility.utilities.kvo.NSObject


public class NavigationInteractor: NSObject(), InteractorProtocol {
    override var entity: ModelObjectProtocol? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = entity, keyPath = "children") { _, _, _  ->
                this.syncChildren()
            }
            changeObservation(from = oldValue, to = entity, keyPath = "actions") { _, _, _  ->
                this.syncActions()
            }
        }
    public var navigation: NavigationModelProtocol?
        get() = entity as? NavigationModelProtocol
        set(newValue) {
            entity = newValue
        }
    public var children: ListInteractor? = null
    public var actions: ListInteractor? = null

    private fun syncChildren() {
        val children = navigation?.children
        if (children != null) {
            val list = this.children ?: ListInteractor()
            list.sync(children)
            this.children = list
        } else {
            this.children = null
        }
    }

    private fun syncActions() {
        val actions = navigation?.actions
        if (actions != null) {
            val list = this.actions ?: ListInteractor()
            list.sync(actions)
            this.actions = list
        } else {
            this.actions = null
        }
    }
}
