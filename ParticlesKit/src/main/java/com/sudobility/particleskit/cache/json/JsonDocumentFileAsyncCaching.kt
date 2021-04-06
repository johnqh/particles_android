package com.sudobility.particleskit.cache.json

import com.sudobility.utilities.bridge.DispatchQueue
import com.sudobility.utilities.json.JsonLoader

open class JsonDocumentFileAsyncCaching(): JsonDocumentFileCaching() {
    open override fun read(path: String, completion: JsonReadCompletionHandler) {
        val file = file(path = path)
        if (file != null) {
            DispatchQueue.global().async {
                val obj = JsonLoader.load(file)
                DispatchQueue.main.async { completion(obj, null) }
            }
        } else {
            completion(null, null)
        }
    }
}
