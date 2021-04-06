package com.sudobility.utilities.utils.featureflags

import com.sudobility.utilities.extensions.MapUtils
import com.sudobility.utilities.utils.debug.Installation

public class CompositeFeatureFlagsProvider: FeatureFlagsProtocol {
    public override val featureFlags: Map<String, Any>?
        get() {
            val localFlags = local?.featureFlags
            val remoteFlags = remote?.featureFlags
            if (localFlags != null) {
                val remoteFlags = remoteFlags
                if (remoteFlags != null) {
                    return MapUtils.merge(remoteFlags, localFlags)
                } else {
                    return localFlags
                }
            } else {
                return remoteFlags
            }
        }

    public override fun activate(completion: () -> Unit) {
        val local = local
        if (local != null) {
            local.activate {
                val remote = this.remote
                if (remote != null) {
                    remote.activate(completion = completion)
                } else {
                    completion()
                }
            }
        }
        if (remote != null) {
            remote?.activate(completion = completion)
        }
    }

    public override fun flag(feature: String?) : Any? {
        if (Installation.appStore) {
            return remote?.flag(feature = feature)
        } else {
            val localFlag = local?.flag(feature = feature)
            if (localFlag != null) {
                return localFlag
            } else {
                return remote?.flag(feature = feature)
            }
        }
    }
    var local: FeatureFlagsProtocol? = null
    var remote: FeatureFlagsProtocol? = null
}
