package com.sudobility.particleskit.presenter.`object`

import android.content.Context
import android.view.ViewGroup
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.utilities.kvo.NSObject


public interface ObjectPresenterProtocol {
    var model: ModelObjectProtocol?
    val selectable: Boolean get() = false
}

public interface SelectableProtocol {
    var isSelected: Boolean
}

open class ObjectPresenter: NSObject(), ObjectPresenterProtocol {
    open var isFirst: Boolean = false
    open var isLast: Boolean = false

    override var model: ModelObjectProtocol? = null
        set(value) {
            field = value
            update()
        }

    open fun update() {
    }
}

public interface ObjectTableCellPresenterProtocol {
    val showDisclosure: Boolean
}
