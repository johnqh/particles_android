package com.sudobility.utilities.kvo

import java.util.*

public interface ObservingProtocol {
    public var _uuid: UUID?
    public val uuid: UUID
        get() {
            if (_uuid == null) {
                _uuid = UUID.randomUUID()
            }
            return _uuid!!
        }

    public fun observe(obj: ObservedProtocol?, keyPath: String, function: (String, Any?, Any?) -> Unit) {
        obj?.kvoController?.observed(this, keyPath, function)
    }

    public fun unobserve(obj: ObservedProtocol?, keyPath: String) {
        obj?.kvoController?.unobserved(this, keyPath)
    }

    public fun changeObservation(from: ObservedProtocol?, to: ObservedProtocol?, keyPath: String, change: (String, Any?, Any?) -> Unit) {
        unobserve(from, keyPath)
        observe(to, keyPath, change)
    }
}