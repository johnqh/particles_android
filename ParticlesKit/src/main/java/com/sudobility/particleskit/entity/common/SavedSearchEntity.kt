package com.sudobility.particleskit.entity.common

import com.sudobility.particleskit.entity.entity.DictionaryEntity

open class SavedSearchEntity: DictionaryEntity() {
    open var name: String?
        get() = parser.asString(value("name"))
        set(value) {
            set(value, "name")
        }

    open var text: String?
        get() = parser.asString(value("text"))
        set(value) {
            set(value, "text")
        }

    open var filters: Map<String, Any>?
        get() = parser.asDictionary(data?.get("fitlers"))

        set(value) {
            set(value, "fitlers")
        }
}
