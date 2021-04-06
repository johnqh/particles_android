package com.sudobility.particleskit.interactor.localdata

import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.utilities.utils.featureflags.FeatureFlagsProtocol

final class LocalFeatureFlagsCacheInteractor: LocalEntityCacheInteractor(), FeatureFlagsProtocol {
    companion object {
        public var shared: LocalFeatureFlagsCacheInteractor = shared()

        fun shared(): LocalFeatureFlagsCacheInteractor {
            val interactor = LocalFeatureFlagsCacheInteractor()
            interactor.key = "features"
            interactor.defaultJson = "features_default.json"
            return interactor
        }

        public fun mock() : LocalFeatureFlagsCacheInteractor {
            val featurFlags = shared
            featurFlags.key = "mock"
            return featurFlags
        }
    }


    override var featureFlags: Map<String, Any>?
        get() = (entity as? DictionaryEntity)?.force?.data
        set(value) {
            (entity as? DictionaryEntity)?.data = value?.toMutableMap()
        }

    override fun activate(completion: () -> Unit) {
        completion()
    }

    override fun flag(feature: String?) : Any? {
        val feature = feature
        if (feature != null) {
            val value = featureFlags?.get(feature)
            if (value != null) {
                if ((value as? String) == "<null>") {
                    return null
                }
                return value
            }
        }
        return null
    }
}
