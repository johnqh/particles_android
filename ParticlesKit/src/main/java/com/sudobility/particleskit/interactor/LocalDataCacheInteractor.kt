package com.sudobility.particleskit.interactor

import com.sudobility.particleskit.entity.model.LocalCacheProtocol
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.loader.LoaderProtocol
import com.sudobility.utilities.kvo.NSObject
import java.util.*

open class LocalJsonCacheInteractor: NSObject, LocalCacheProtocol {
    open var key: String? = null
    open var defaultJson: String? = null
    private var _loader: LoaderProtocol? = null
    val loader: LoaderProtocol?
        get() {
            if (_loader == null) {
                _loader = createLoader()
            }
            return _loader
        }
    val path: String?
        get() {
            val key = key
            if (key != null) {
                return "${className}.persist.${key}"
            }
            return null
        }

    constructor(key: String? = null, defaultJson: String? = null) : super() {    this.key = key
        this.defaultJson = defaultJson
        load()
    }

    open fun createLoader() : LoaderProtocol? =
            null

    open fun load() {
        loader?.load(params = null, completion = { obj, loadTime, error  ->
            this.receive(obj, loadTime, error)
        })
    }

    override fun entity(data: Map<String, Any>?) : ModelObjectProtocol? =
            null

    open fun receive(obj: Any?, loadTime: Date?, error: Error?) {}

    open fun save() {}
}
