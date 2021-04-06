package com.sudobility.particleskit.error.log

import com.sudobility.utilities.Protocols.NSObjectProtocol

open interface ErrorLoggingProtocol: NSObjectProtocol {
    fun log(error: Error?)
}

public class ErrorLogging {
    companion object {
        var shared: ErrorLoggingProtocol? = null
    }
}
