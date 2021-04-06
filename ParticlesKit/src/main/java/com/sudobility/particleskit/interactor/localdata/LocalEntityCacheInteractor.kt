package com.sudobility.particleskit.interactor.localdata

import com.sudobility.particleskit.cache.json.JsonDocumentFileCaching
import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.LocalJsonCacheInteractor
import com.sudobility.particleskit.interactor.protocol.InteractorProtocol
import com.sudobility.particleskit.loader.Loader
import com.sudobility.particleskit.loader.LoaderProtocol
import java.util.*


open class LocalEntityCacheInteractor: LocalJsonCacheInteractor, InteractorProtocol {
    override var entity: ModelObjectProtocol? = null
        set(value) {
            val oldValue = entity
            set(value, "entity")
            changeObservation(from = oldValue, to = entity, keyPath = "data") { _, _, _  ->
                this.save()
            }
        }
    val dictionaryEntity: DictionaryEntity?
        get() = entity as? DictionaryEntity

    constructor() : super() {}

    constructor(key: String? = null, defaultJson: String? = null) : super(key = key, defaultJson = defaultJson) {}

    override var key: String? = null

    open override fun createLoader() : LoaderProtocol? {
        val path = path
        if (path != null) {
            return Loader("DictionaryEntity", path = path, io = listOf(JsonDocumentFileCaching()), cache = this)
        }
        return null
    }

    open override fun entity(data: Map<String, Any>?) : ModelObjectProtocol? =
            entity ?: entityObject()

    open fun entityObject() : ModelObjectProtocol =
            DictionaryEntity()

    open override fun receive(obj: Any?, loadTime: Date?, error: Error?) {
        if (error == null) {
            process(obj as? ModelObjectProtocol)
        }
    }

    open fun process(obj: ModelObjectProtocol?) {
        if (obj != null) {
            entity = obj
        } else {
            entity = entityObject()
        }
    }

    open override fun save() {
        loader?.save(entity)
    }
}
