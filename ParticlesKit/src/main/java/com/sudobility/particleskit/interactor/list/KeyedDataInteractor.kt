package com.sudobility.particleskit.interactor.list

import com.sudobility.particleskit.entity.model.FilterableProtocol
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.utilities.utils.Debouncer

class ModelObjectComparator {
    companion object : Comparator<ModelObjectProtocol> {
        override fun compare(a: ModelObjectProtocol, b: ModelObjectProtocol): Int {
            return if (a.order(b)) {
                1
            } else {
                0
            }
        }
    }
}

open class KeyedDataInteractor: DataPoolInteractor() {
    var filter: String? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (filter != oldValue) {
                filterChanged()
            }
        }
    override open var data: Map<String, ModelObjectProtocol>? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            dataChanged()
        }
    open var transformed: Map<String, ModelObjectProtocol>? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            transformedChanged()
        }
    open var list: ListInteractor? = null

    open fun dataChanged() {
        transformed = data
    }
    var transformDebouncer: Debouncer = Debouncer()

    open fun filterChanged() {
        transformedChanged()
    }

    open fun transformedChanged() {
        val transformed = this.transformed
        var filtered: Map<String, ModelObjectProtocol>? = null
        var sorted: List<ModelObjectProtocol>? = null
        val handler = transformDebouncer.debounce()
        handler?.run(background = {
            filtered = this.filter(objects = transformed, filter = this.filter)
        }, then = {
            sorted = this.sort(objects = filtered)
        }, final = {
            val sorted = sorted
            if (sorted != null) {
                val list = this.list ?: ListInteractor()
                list.sync(sorted)
                this.list = list
            } else {
                this.list = null
            }
        }, delay = 0.1F)
    }

    open fun filter(objects: Map<String, ModelObjectProtocol>?, filter: String?) : Map<String, ModelObjectProtocol>? {
        transformed.let {
            val filter = filter?.toLowerCase()?.trim()
                val filtered = mutableMapOf<String, ModelObjectProtocol>()
                for ((key, value) in transformed!!) {
                    val obj = value as ModelObjectProtocol
                    if (this.filter(obj, filter!!)) {
                        filtered[key] = value
                    }
                }
                return filtered
            }
        return null
    }

    open fun sort(objects: Map<String, ModelObjectProtocol>?) : List<ModelObjectProtocol>? {
        val sorted = objects?.values?.toList()
        sorted?.sortedWith(ModelObjectComparator)
        return  sorted
    }

    open fun filter(obj: ModelObjectProtocol, filter: String?) : Boolean =
            (obj as? FilterableProtocol)?.filter(lowercased = filter) ?: false
}
