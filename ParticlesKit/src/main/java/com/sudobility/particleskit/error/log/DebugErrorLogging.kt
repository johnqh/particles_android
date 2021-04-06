package com.sudobility.particleskit.error.log

import com.sudobility.utilities.error.Console
import com.sudobility.utilities.kvo.NSObject


public class DebugErrorLogging: NSObject(), ErrorLoggingProtocol {
    override fun log(error: Error?) {
        val error = error
        if (error != null) {
            Console.shared.log("Error:${error}")
        }
    }
}
