package com.sudobility.particlescommonmodels.browse

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.list.DataPoolInteractor


open class BrowsingListInteractor: FilteredListInteractor() {
    open var dataCache: DataPoolInteractor? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = dataCache, keyPath = "data") { _, _, _  ->
                this.filter()
            }
        }
    open override val data: List<ModelObjectProtocol>?
        get() {
            val values = dataCache?.data?.values
            if (values != null) {
                return values.toList()
            } else {
                return null
            }
        }

    open override fun filter(data: ModelObjectProtocol, key: String, value: Any) : Boolean {
        when (key) {
            else -> return true
        }
    }
}
