package com.sudobility.particleskit.entity.common

import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.utilities.bridge.UserDefaults

open class FilterEntity: DictionaryEntity {
    public override var key: String? = null
    private val preferenceKey: String?
        get() {
            val key = key
            if (key != null) {
                return "${className}.filters.${key}"
            }
            return null
        }
    open override var data: MutableMap<String, Any>? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            val preferenceKey = preferenceKey
            if (preferenceKey != null) {
                UserDefaults.standard.set(data, forKey = preferenceKey)
            }
        }

    constructor(key: String) : super() {
        this.key = key
        val preferenceKey = preferenceKey
        if (preferenceKey != null) {
            data = UserDefaults.standard.dictionary(forKey = preferenceKey)?.toMutableMap() ?: mutableMapOf<String , Any>()
        }
    }

    constructor(entity: FilterEntity) : super() {
        data = entity.data
    }

    fun apply(entity: FilterEntity) {
        entity.data = data
    }
}
