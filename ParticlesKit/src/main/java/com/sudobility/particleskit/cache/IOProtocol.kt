package com.sudobility.particleskit.cache

import com.sudobility.utilities.Protocols.NSObjectProtocol

typealias IOReadCompletionHandler = (data: Any?, meta: Any?, priority: Int, error: Error?) -> Unit
typealias IOWriteCompletionHandler = (data: Any?, error: Error?) -> Unit
typealias IODeleteCompletionHandler = (error: Error?) -> Unit

interface IOProtocol: NSObjectProtocol {
    var priority: Int
    fun load(path: String, params: Map<String, Any>?, completion: IOReadCompletionHandler)
    fun save(path: String, params: Map<String, Any>?, data: Any?, completion: IOWriteCompletionHandler?)
    fun modify(path: String, params: Map<String, Any>?, data: Any?, completion: IOWriteCompletionHandler?)
    fun delete(path: String, params: Map<String, Any>?, completion: IODeleteCompletionHandler?)
}
