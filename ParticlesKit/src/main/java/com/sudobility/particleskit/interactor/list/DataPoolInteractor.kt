package com.sudobility.particleskit.interactor.list

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.LocalJsonCacheInteractor
import com.sudobility.utilities.bridge.NSError
import com.sudobility.utilities.utils.Debouncer
import java.util.*


open class DataPoolInteractor: LocalJsonCacheInteractor() {
    open var data: Map<String, ModelObjectProtocol>? = null
    private var saveDebouncer: Debouncer = Debouncer()

    override open fun receive(obj: Any?, loadTime: Date?, error: Error?) {
        val error = error
        if (error != null) {
            if ((error as NSError).code == 403 || (error as NSError).code == 204) {
                data = null
            } else {
                val temp = data
                data = temp
            }
        } else {
            val entities = obj as? List<ModelObjectProtocol>
            if (entities != null) {
                var parsed = mutableMapOf<String , ModelObjectProtocol>()
                for (entity in entities) {
                    val key = entity.key
                    if (key != null) {
                        parsed[key] = entity
                    }
                }
                data = parsed
                save()
            } else {
                data = mapOf()
            }
        }
    }

    override open fun save() {
        val handler = saveDebouncer.debounce()
        if (handler != null) {
            handler.run({
                this.loader?.save(this.data)
            }, delay = 1.0F)
        }
    }
}
