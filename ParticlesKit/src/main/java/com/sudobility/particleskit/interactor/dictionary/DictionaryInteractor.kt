package com.sudobility.particleskit.interactor.dictionary

import com.sudobility.utilities.kvo.NSObject


open class DictionaryInteractor: NSObject() {
    open var dictionary: MutableMap<String, Any> = mutableMapOf()

    override fun set(value: Any?, key: String) {
        val _key = "dictionary"
        val oldValue = dictionary[key]
        val value = value
        if (value != null) {
            val oldValue = oldValue
            if (oldValue != null) {
                val obj = value as? NSObject
                val oldObject = oldValue as? NSObject
                if (obj != null && oldObject != null) {
                    if (obj !== oldObject) {
                        willChangeValue(forKey = _key)
                        dictionary[key] = value
                        didChangeValue(forKey = _key)
                    }
                } else {
                    willChangeValue(forKey = _key)
                    dictionary[key] = value
                    didChangeValue(forKey = _key)
                }
            } else {
                willChangeValue(forKey = _key)
                dictionary[key] = value
                didChangeValue(forKey = _key)
            }
        } else {
            if (oldValue != null) {
                willChangeValue(forKey = _key)
                dictionary.remove(key)
                didChangeValue(forKey = _key)
            }
        }
    }
}
