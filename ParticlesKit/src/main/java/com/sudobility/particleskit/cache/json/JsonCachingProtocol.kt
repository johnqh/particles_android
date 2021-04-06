package com.sudobility.particleskit.cache.json

import com.sudobility.particleskit.cache.IODeleteCompletionHandler
import com.sudobility.particleskit.cache.IOProtocol
import com.sudobility.particleskit.cache.IOReadCompletionHandler
import com.sudobility.particleskit.cache.IOWriteCompletionHandler

public typealias JsonReadCompletionHandler = (data: Any?, error: Error?) -> Unit
public typealias JsonWriteCompletionHandler = (error: Error?) -> Unit

public interface JsonCachingProtocol: IOProtocol {
    fun read(path: String, completion: JsonReadCompletionHandler)
    fun write(path: String, data: Any?, completion: JsonWriteCompletionHandler?)
    override fun load(path: String, params: Map<String, Any>?, completion: IOReadCompletionHandler) {
        read(path = path) { data, error  ->
            completion(data, null, this.priority, error)
        }
    }

    override fun save(path: String, params: Map<String, Any>?, data: Any?, completion: IOWriteCompletionHandler?) {
        write(path = path, data = data) { error  ->
            completion?.invoke(data, error)
        }
    }

    override fun modify(path: String, params: Map<String, Any>?, data: Any?, completion: IOWriteCompletionHandler?) {
        write(path = path, data = data) { error  ->
            completion?.invoke(data, error)
        }
    }

    override fun delete(path: String, params: Map<String, Any>?, completion: IODeleteCompletionHandler?) {
    }
}
