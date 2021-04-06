package com.sudobility.utilities.tracker

import com.sudobility.utilities.error.Console

public class DebugTracking: TrackingProtocol {
    override var userInfo: Map<String, String>? = null
    override var excluded: Boolean = false

    override fun path(path: String?, data: Map<String, Any>?) {
        val path = path
        if (path != null) {
            if (excluded) {
                Console.shared.log("Excluded Path:${path}", data ?: "")
            } else {
                Console.shared.log("Path:${path}", data ?: "")
            }
        }
    }

    override fun path(path: String?, action: String?, data: Map<String, Any>?) {
        val path = path
        val action = action
        if (path != null && action != null) {
            if (excluded) {
                Console.shared.log("Excluded Path:${path} Action:${action}", data ?: "")
            } else {
                Console.shared.log("Path:${path} Action:${action}", data ?: "")
            }
        }
    }

    override fun log(event: String, data: Map<String, Any>?) {
        Console.shared.log("event:${event}, ", data ?: "")
    }
}
