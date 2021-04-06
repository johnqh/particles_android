package com.sudobility.particleskit.entity.entity

import com.sudobility.particleskit.entity.model.DirtyProtocol
import com.sudobility.particleskit.entity.model.JsonPersistable
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.utilities.Protocols.ParsingProtocol
import com.sudobility.utilities.extensions.MapUtils
import com.sudobility.utilities.kvo.NSObject
import java.util.*


open class DictionaryEntity: NSObject(), ModelObjectProtocol, ParsingProtocol, JsonPersistable,
    DirtyProtocol {
    open var data: MutableMap<String, Any>? = null
    override open var dirty_time: Date?
        get() = parser.asDate(value("dirty_time"))
        set(value) {
            set(value, "dirty_time")
        }

    override var dirty: Boolean
        get() = dirty_time != null
        set(value) {
            if (value) {
                dirty_time = Date()
            } else {
                dirty_time = null
            }
        }

    val force: DictionaryEntity
        get() {
            if (data == null) {
                data = mutableMapOf()
            }
            return this
        }

    override var json: Map<String, Any>?
        get() = data
        set(newValue) {
            val dictionary = newValue
            if (dictionary != null) {
                parse(dictionary = dictionary)
            }
        }

    override val thinned: Map<String, Any>?
        get() = json

    override fun parse(dictionary: Map<String, Any>) {
        var keys = dictionary.keys
        for (key in keys) {
            willChangeValue(forKey = key)
        }
        data = MapUtils.merge(data, dictionary)?.toMutableMap()
        for (key in keys) {
            didChangeValue(forKey = key)
        }
    }

    override val displayTitle: String?
        get() = parser.asString(this.data?.get("title"))

    override open fun value(key: String) : Any? =
        data?.get(key)

    override fun set(value: Any?, key: String) {
        willChangeValue(forKey = key)
        willChangeValue(forKey = "data")
        if (value != null) {
            force.data?.set(key, value!!)
        } else {
            data?.remove(key)
        }
        didChangeValue(forKey = "data")
        didChangeValue(forKey = key)
    }
}
