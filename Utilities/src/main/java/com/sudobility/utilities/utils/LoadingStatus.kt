package com.sudobility.utilities.utils

import com.sudobility.utilities.Protocols.NSObjectProtocol
import com.sudobility.utilities.kvo.NSObject

public interface LoadingStatusProtocol: NSObjectProtocol {
    var running: Boolean
}

public class LoadingStatus: NSObject(), LoadingStatusProtocol {
    companion object {
        public var shared: LoadingStatus = LoadingStatus()
    }
    override var running: Boolean
        get() = value("running") as? Boolean ?: false
        set(value) = set(value, "running")

    private var runningCount: Int = 0
        set(value) {
            val oldValue = field
            field = value
            if (runningCount != oldValue) {
                running = runningCount > 0
            }
        }

    fun plus() {
        runningCount += 1
    }

    fun minus() {
        runningCount -= 1
    }
}
