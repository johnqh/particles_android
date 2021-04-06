package com.sudobility.particleskit.interactor.list

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.LocalJsonCacheInteractor
import java.util.*


open class DataListInteractor: LocalJsonCacheInteractor() {
    open var data: List<ModelObjectProtocol>? = null

    open override fun receive(obj: Any?, loadTime: Date?, error: Error?) {
        if (error == null) {
            data = obj as? List<ModelObjectProtocol>
        }
    }
}
