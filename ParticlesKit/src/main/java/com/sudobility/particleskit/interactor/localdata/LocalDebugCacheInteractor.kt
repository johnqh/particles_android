package com.sudobility.particleskit.interactor.localdata

import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.utilities.utils.debug.DebugProtocol


final class LocalDebugCacheInteractor: LocalEntityCacheInteractor(), DebugProtocol {
    companion object {
        public var shared: LocalDebugCacheInteractor = shared()

        fun shared() : LocalDebugCacheInteractor {
            val interactor = LocalDebugCacheInteractor()
            interactor.key = "debug"
            interactor.defaultJson = "debug_default.json"
            return interactor
        }

        public fun mock() : LocalDebugCacheInteractor {
            val debug = shared
            debug.key = "mock"
            debug.debug = mapOf("api_replay" to "p", "integration_test" to "t")
            return debug
        }
    }


    override var debug: Map<String, Any>?
        get() = (entity as? DictionaryEntity)?.force?.data
        set(value) {
            (entity as? DictionaryEntity)?.data = value?.toMutableMap()
        }
}
