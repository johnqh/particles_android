package com.sudobility.utilities.kvo

import java.util.*
import kotlin.collections.HashMap

typealias FBKVONotificationBlock = (String, Any?, Any?) -> Unit

class KVOController {
    // nested, keyPath first, then uuid
    private var observing = HashMap<String, HashMap<UUID, FBKVONotificationBlock?>?>()

    internal fun observed(obj: ObservingProtocol?, keyPath: String, function: FBKVONotificationBlock?) {
        val obj = obj?.let {
            var map = observing[keyPath]
            if (map != null) {
                map[obj.uuid] = function
            } else {
                val map = HashMap<UUID, FBKVONotificationBlock?>()
                map[obj.uuid] = function
                observing[keyPath] = map
            }
            function?.invoke(keyPath, null, null)
        }
    }

    internal fun unobserved(obj: ObservingProtocol?, keyPath: String) {
        val obj = obj?.let {
            var map = observing[keyPath]
            if (map != null) {
                map[obj.uuid] = null
            }
        }
    }

    internal fun change(keyPath: String, oldValue: Any?, newValue: Any?) {
        val observingPath: HashMap<UUID, FBKVONotificationBlock?>? = observing[keyPath]
        observingPath?.let {
            observingPath?.keys.forEach {
                val key = it
                val function =  observingPath!![key]
                function?.invoke(keyPath, oldValue, newValue)
            }
        }
    }
}