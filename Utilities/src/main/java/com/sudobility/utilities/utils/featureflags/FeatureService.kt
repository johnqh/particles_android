package com.sudobility.utilities.utils.featureflags

open interface FeatureFlagsProtocol {
    open val featureFlags: Map<String, Any>?
    open fun activate(completion: () -> Unit)
    open fun flag(feature: String?) : Any?
}

public class FeatureService {
    public companion object {
        public var shared: FeatureFlagsProtocol? = null
    }
}
