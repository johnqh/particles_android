package com.sudobility.particleskit.interactor.`object`

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.list.DataPoolInteractor
import com.sudobility.particleskit.interactor.protocol.LoadingInteractorProtocol
import com.sudobility.particleskit.loader.LoaderProtocol
import com.sudobility.utilities.kvo.NSObject


open class LoadingObjectInteractor: NSObject(), LoadingInteractorProtocol {
    open var listManager: DataPoolInteractor? = null
    open var loader: LoaderProtocol? = null
    override var objectKey: String? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (objectKey != oldValue) {
                loadEntity()
            }
        }
    open val loadingParams: Map<String, Any>?
        get() = null
    override var entity: ModelObjectProtocol? = null

    open fun loadEntity() {
        if (objectKey != null) {
            entity = listManager?.data?.get(objectKey!!)
        } else {
            entity = null
        }
        val loadingParams = loadingParams
        val objectKey = objectKey
        if (loadingParams != null && objectKey != null) {
            loader?.load(params = loadingParams, completion = { entity, _, _  ->
                if (this.objectKey == objectKey) {
                    this.entity = entity as? ModelObjectProtocol
                }
            })
        }
    }
}
