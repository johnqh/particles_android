package com.sudobility.particlescommonmodels.like

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.list.DataPoolInteractor
import com.sudobility.particleskit.interactor.list.ListInteractor


open class DislikedListInteractor: ListInteractor {
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
            changeObservation(from = oldValue, to = likedManager, keyPath = "disliked") { _, _, _  ->
                this.update()
            }
        }

    constructor() : super() {
        setup()
    }

    open fun setup() {}

    open fun update() {
        val data = this.dataCache?.data
        val disliked = this.likedManager?.disliked?.toMutableList()
        if (data != null && disliked != null) {
            var dislikes = mutableListOf<ModelObjectProtocol>()
            for (key in disliked) {
                val obj = data[key]
                if (obj != null) {
                    dislikes.add(obj)
                }
            }
            sync(dislikes)
        } else {
            sync(null)
        }
    }
}
