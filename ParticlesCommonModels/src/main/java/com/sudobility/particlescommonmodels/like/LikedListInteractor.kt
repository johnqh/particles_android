package com.sudobility.particlescommonmodels.like

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.list.DataPoolInteractor
import com.sudobility.particleskit.interactor.list.ListInteractor
import com.sudobility.utilities.extensions.compactMap

open class LikedListInteractor: ListInteractor {
    open var dataCache: DataPoolInteractor? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = dataCache, keyPath = "data") { _, _, _  ->
                this.update()
            }
        }
    open var likedManager: LikedKeysInteractor? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = likedManager, keyPath = "liked") { _, _, _  ->
                this.update()
            }
        }

    constructor() : super() {
        setup()
    }

    open fun setup() {}

    open fun update() {
        val data = this.dataCache?.data
        val liked = this.likedManager?.liked
        if (data != null && liked != null) {
            val favorites = liked.compactMap { key  ->
                filter(obj = data[key] as? ModelObjectProtocol)
            }
            sync(favorites)
        } else {
            sync(null)
        }
    }

    open fun filter(obj: ModelObjectProtocol?) : ModelObjectProtocol? =
        obj
}
