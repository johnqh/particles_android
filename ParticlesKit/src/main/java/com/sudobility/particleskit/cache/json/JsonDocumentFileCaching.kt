package com.sudobility.particleskit.cache.json

import com.sudobility.utilities.bridge.NSError
import com.sudobility.utilities.json.JsonLoader
import com.sudobility.utilities.json.JsonWriter
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.Debouncer

open class JsonDocumentFileCaching: NSObject, JsonCachingProtocol {
    override var priority: Int = 0
    val x = 4
    var debouncer: Debouncer = Debouncer()
    var folder: String? = null

    fun file(path: String) : String? {
        return  null
//        if (folder == null) {
//            folder = FolderService.shared?.documents()
//        }
//        return folder?.stringByAppendingPathComponent(path = path)?.stringByAppendingPathComponent(path = "data.json")
    }

    constructor(priority: Int = 0) : super() {
        this.priority = priority
    }

    override fun read(path: String, completion: JsonReadCompletionHandler) {
        val file = file(path = path)
        if (file != null) {
            val obj = JsonLoader.load(file)
            completion(obj, null)
        } else
        {
            completion(null, null)
        }
    }

    override fun write(path: String, data: Any?, completion: JsonWriteCompletionHandler?) {
        val file = file(path = path)
        if (file != null) {
            val handler = debouncer.debounce()
            if (handler != null) {
                handler.run(background = { JsonWriter.write(data, file) }, final = { completion?.invoke(null) }, delay = 0.5F)
            }
        } else {
            completion?.invoke(NSError(0, mapOf("message" to "document folder error")))
        }
    }
}
