package com.sudobility.particleskit.interactor.list

import com.sudobility.particleskit.entity.model.ModelListProtocol
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.utilities.kvo.NSObject


open class ListInteractor: NSObject(), ModelListProtocol {
    open var loading: Boolean = false
    open var title: String? = null
    override var parent: ModelObjectProtocol? = null
    override val displayTitle: String?
        get() = parent?.displayTitle ?: null
    override val displaySubtitle: String?
        get() = parent?.displaySubtitle ?: null
    override val displayImageUrl: String?
        get() = parent?.displayImageUrl ?: null
    override var list: List<ModelObjectProtocol>? = null
    open var prefixObj: ModelObjectProtocol? = null
    open var postfixObj: ModelObjectProtocol? = null
    open val count: Int
        get() = list?.size ?: 0

    private fun indexOf(sourceObject: ModelObjectProtocol?, inside: List<ModelObjectProtocol>?, startIndex: Int) : Int? {
        if (inside != null) {
            return sourceObject?.let { inside.indexOf(it) }
        }
        return null
    }

    private fun compare(destination: ModelObjectProtocol?, source: ModelObjectProtocol?) : Boolean {
        return (destination === source)
    }

    open fun sync(list: List<ModelObjectProtocol>?) {
            this.list = list
    }

    open fun move(from: Int, to: Int, update: Boolean = false) {
        val item = list?.get(from)
        if (item != null && from != to) {
            var list = this.list?.toMutableList()
            if (list != null) {
                list.removeAt(from)
                list.add(to, item)
                this.list = list
            }
        }
    }
}
