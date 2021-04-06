package com.sudobility.utilities.utils.debug

public interface DebugProtocol {
    public var debug: Map<String, Any>?
}

public object DebugSettings: DebugProtocol {
    override var debug: Map<String, Any>? = null
}
