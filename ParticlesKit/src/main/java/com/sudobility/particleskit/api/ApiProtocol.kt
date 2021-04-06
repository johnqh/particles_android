package com.sudobility.particleskit.api

import com.sudobility.particleskit.cache.IODeleteCompletionHandler
import com.sudobility.particleskit.cache.IOProtocol
import com.sudobility.particleskit.cache.IOReadCompletionHandler
import com.sudobility.particleskit.cache.IOWriteCompletionHandler

public typealias ApiCompletionHandler = (data: Any?, error: Error?) -> Unit
// for upload or download
public typealias ApiProgressHandler = (progress: Float) -> Unit

public interface ApiProtocol: IOProtocol {
    fun get(path: String, params: Map<String, Any>?, completion: ApiCompletionHandler)
    fun post(path: String, params: Map<String, Any>?, data: Any?, completion: ApiCompletionHandler)
    fun put(path: String, params: Map<String, Any>?, data: Any?, completion: ApiCompletionHandler)
    fun delete(path: String, params: Map<String, Any>?, completion: ApiCompletionHandler)

    override fun load(path: String, params: Map<String, Any>?, completion: IOReadCompletionHandler) {
        get(path = path, params = params) { data, error ->
            completion(data, null, this.priority ?: 10, error)
        }
    }

    override fun save(path: String, params: Map<String, Any>?, data: Any?, completion: IOWriteCompletionHandler?) {
        post(path = path, params = params, data = data) { data, error ->
            completion?.invoke(data, error)
        }
    }

    override fun modify(path: String, params: Map<String, Any>?, data: Any?, completion: IOWriteCompletionHandler?) {
        put(path = path, params = params, data = data) { data, error ->
            completion?.invoke(data, error)
        }
    }

    override fun delete(path: String, params: Map<String, Any>?, completion: IODeleteCompletionHandler?) {
        delete(path = path, params = params) { _, error ->
            completion?.invoke(error)
        }
    }
}