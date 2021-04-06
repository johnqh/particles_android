package com.sudobility.jediokit.field

import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.utilities.utils.parser.Parser
import com.sudobility.utilities.utils.parser.featureFlagged

open class FieldDefinition: DictionaryEntity() {
    public fun definition(field: String): Map<String, Any>? =
            data?.get(field) as? Map<String, Any>

    public fun set(definition: Map<String, Any>?, field: String) {
        if (definition != null) {
            data?.set(field, definition)
        } else {
            data?.remove(field)
        }
    }

    override open val parser: Parser
        get() = Parser.featureFlagged


    val visible: Boolean
        get() = parser.asBoolean(data?.get("visible")) ?: false
    val title: Map<String, Any>?
        get() = definition("title")
    val subtitle: Map<String, Any>?
        get() = definition("subtitle")
    val text: Map<String, Any>?
        get() = definition("text")
    val subtext: Map<String, Any>?
        get() = definition("subtext")
    val image: Map<String, Any>?
        get() = definition("image")
    val link: Map<String, Any>?
        get() = definition("link")
    val dependencies: Map<String, Any>?
        get() = definition("dependencies")
}

open class FieldOutputDefinition: FieldDefinition() {
    val checked: Map<String, Any>?
        get() = definition("checked")
    val strings: Map<String, Any>?
        get() = definition("strings")
    val images: Map<String, Any>?
        get() = definition("images")
    var items: List<FieldOutputDefinition>? = null

    override open fun parse(dictionary: Map<String, Any>) {
        super.parse(dictionary = dictionary)
        val items = parser.asArray(dictionary["items"]) as? List<Map<String, Any>>
        if (items != null) {
            var children = mutableListOf<FieldOutputDefinition>()
            for (item in items) {
                val child = FieldOutputDefinition()
                child.parse(dictionary = item)
                children.add(child)
            }
            this.items = children
        }
    }
}
enum class FieldType {
    text,
    strings,
    int,
    float,
    bool,
    percent,
    image,
    images,
    signature
}
enum class FieldValidateType (val rawValue: String) {
    email("email"), password("password"), phone("phone"), url("url"), creditcard("creditcard");

    companion object {
        operator fun invoke(rawValue: String) = FieldValidateType.values().firstOrNull { it.rawValue == rawValue }
    }
}

open class FieldInputDefinition: FieldDefinition() {
    var field: Map<String, Any>?
        get() = definition("field")
        set(value) {
            set(value, "field")
        }

    val fieldType: FieldType
        get() {
            return when (parser.asString(this.field?.get("type"))) {
                "text" -> FieldType.text
                "strings" -> FieldType.strings
                "int" -> FieldType.int
                "float" -> FieldType.float
                "bool" -> FieldType.bool
                "percent" -> FieldType.percent
                "image" -> FieldType.image
                "images" -> FieldType.images
                "signature" -> FieldType.signature
                else -> FieldType.strings
            }
        }

    val defaultValue: Any?
        get() = this.field?.get("default")

    val isOptional: Boolean
        get() = parser.asBoolean(this.field?.get("optional")) ?: false

    val validator: FieldValidateType?
        get() {
            val string = parser.asString(this.field?.get("validator"))
            if (string != null) {
                return FieldValidateType(rawValue = string)
            }
            return null
        }

    var options: List<Map<String, Any>>?
        get() = this.field?.get("options") as? List<Map<String, Any>>
        set(value) {
            willChangeValue(forKey = "options")
            val mutable = this.field?.toMutableMap()
            if (value !== null) {
                mutable?.set("options", value)
            } else {
                mutable?.remove("options")
            }
            this.field = mutable
            didChangeValue(forKey = "options")
        }

    val min: Float?
        get() = parser.asFloat(this.field?.get("min"))
    val max: Float?
        get() = parser.asFloat(this.field?.get("max"))

    fun option(value: Any) : Map<String, Any>? =
        options?.firstOrNull { option  ->
            val itemValue = option["value"]
            if (itemValue != null) {
                if (value.javaClass.name == itemValue.javaClass.name) {
                    if (value is String) {
                        value as? String == itemValue as? String
                    } else if (value is Int) {
                        value as? Int == itemValue as? Int
                    } else {
                        false
                    }
                }
            }
            false
        } ?: null

    fun option(label: String) : Map<String, Any>? =
        options?.firstOrNull  { option  ->
            parser.asString(option["text"]) == label
        } ?: null
}
