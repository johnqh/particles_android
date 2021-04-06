package com.sudobility.particleskit.error.log

import com.sudobility.utilities.kvo.NSObject


public class CompositeErrorLogging: NSObject(), ErrorLoggingProtocol {
    private var loggings: MutableList<ErrorLoggingProtocol> = mutableListOf<ErrorLoggingProtocol>()

    fun add(logging: ErrorLoggingProtocol?) {
        val aLogging = logging
        if (aLogging != null) {
            loggings.add(aLogging)
        }
    }

    override fun log(error: Error?) {
        for (logging: ErrorLoggingProtocol in loggings) {
            logging.log(error)
        }
    }
}
