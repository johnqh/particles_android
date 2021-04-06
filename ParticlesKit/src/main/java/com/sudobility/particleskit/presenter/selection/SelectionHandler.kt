package com.sudobility.particleskit.presenter.selection

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.routingkit.router.Router
import com.sudobility.routingkit.router.RoutingOriginatorProtocol


public interface SelectionHandlerProtocol {
    fun select(obj: ModelObjectProtocol?) : Boolean
    fun deselect(obj: ModelObjectProtocol?)
}

public interface PersistSelectionHandlerProtocol: SelectionHandlerProtocol {
    val singleSelected: ModelObjectProtocol?
    var selected: List<ModelObjectProtocol>?
    var multipleSelection: Boolean
}

open class RoutingSelectionHandler: SelectionHandlerProtocol {
    override fun select(obj: ModelObjectProtocol?): Boolean {
        val origintor = obj as? RoutingOriginatorProtocol
        if (origintor != null) {
            Router.shared?.navigate(origintor, true, null)
        }
        return false
    }

    override fun deselect(obj: ModelObjectProtocol?) {
    }
}