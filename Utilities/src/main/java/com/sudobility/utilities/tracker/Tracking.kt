package com.sudobility.utilities.tracker


public interface TrackingProtocol {
    var userInfo: Map<String, String>?
    var excluded: Boolean
    fun path(path: String?, data: Map<String, Any>?)
    fun path(path: String?, action: String?, data: Map<String, Any>?)
    fun log(event: String, data: Map<String, Any>?)
}

public class Tracking {
    companion object {
        public var shared: TrackingProtocol? = null
    }
}
