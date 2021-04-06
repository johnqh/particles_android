package com.sudobility.particleskit.presenter.list

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.list.ListInteractor
import com.sudobility.particleskit.presenter.selection.SelectionHandlerProtocol
import com.sudobility.utilities.kvo.ObservingProtocol
import java.util.*

open class ListPresenter: ObservingProtocol {
    public var selectionHandler: SelectionHandlerProtocol? = null
    public override var _uuid: UUID? = null

    public var listInteractor: ListInteractor? = null
        set(value: ListInteractor?) {
            val old = listInteractor
            field = value
            changeObservation(old, value, "list") {_, _, _ ->
                update(value?.list)
            }
        }

    open fun update(items: List<ModelObjectProtocol>?) {

    }
}