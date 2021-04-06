package com.sudobility.jediokit.field

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.protocol.InteractorProtocol
import com.sudobility.utilities.kvo.NSObject

interface FieldProtocol: InteractorProtocol {
    var field: FieldDefinition?

    val title: String?
        get() = text(this.field?.title)
    val subtitle: String?
        get() = text(this.field?.subtitle)
    val text: String?
        get() = text(this.field?.text)
    val subtext: String?
        get() = text(this.field?.subtext)
    val image: String?
        get() = text(this.field?.image)
    val dependencies: Map<String, Any>?
        get() = this.field?.dependencies

    fun bool(definition: Map<String, Any>?) : Boolean? {
        val text = text(definition)?.toLowerCase()
        if (text != null) {
            if (text == "y" || text == "1" || text == "true" || text == "yes") {
                return true
            } else if (text == "n" || text == "0" || text == "false" || text == "no") {
                return false
            }
        }
        return null
    }

    fun hasData(definition: Map<String, Any>?, textOK: Boolean = false): Boolean {
        val entity = entity
        val definition = definition
        if (entity != null && definition != null) {
            if (parser.asString(definition["text"]) != null) {
                return textOK
            } else {
                val field = parser.asString(definition?.get("field"))
                if (field != null) {
                    val entity = entity as? NSObject
                    return parser.asString(entity?.value(field)) != null
                }
            }
        }
        return false
    }

    fun text(definition: Map<String, Any>?) : String? {
        val value = textValue(definition)
        val definition = definition
        if (value != null && definition != null) {
            val options = parser.asDictionary(definition["options"])
            val text = parser.asString(options?.get(value))
            if (options != null && text != null) {
                return text
            } else {
                return value
            }
        }
        return null
    }

    fun textValue(definition: Map<String, Any>?) : String? {
        val entity = entity as? ModelObjectProtocol
        val definition = definition
        if (entity != null && definition != null) {
            val text = parser.asString(definition["text"])
            if (text != null) {
                return text
            } else {
                val field = parser.asString(definition["field"])
                if (field != null) {
                    val value = entity.value(field)
                    if (value != null) {
                        when (parser.asString(definition["type"])) {
                            "bool" -> return parser.asString(value)
                            "int" -> return parser.asInt(value)?.toString()
                            "float" -> return parser.asFloat(value)?.toString()
                            "percent" -> {
                                val float = parser.asFloat(value)
                                if (float != null) {
                                    val percent = float * 100
                                    return percent.toString()
//                                return String(format = "%.2f%%", percent)
                                } else {
                                    return null
                                }
                            }
                            "text" -> return parser.asString(value)
                            else -> return parser.asString(value)
                        }
                    }
                }
            }
        }
        return null
    }

fun strings(definition: Map<String, Any>?) : List<String>? {
    val entity = entity as? ModelObjectProtocol
    val definition = definition
    val field = parser.asString(definition?.get("field"))
    if (entity != null && definition != null && field != null) {
        return parser.asStrings(entity.value(field))
    }
    return null
}
}

interface FieldOutputProtocol: FieldProtocol {
    val checked: Boolean?
    val items: List<FieldOutputProtocol>?
    val hasData: Boolean

    val FieldOutputProtocol.fieldOutput: FieldOutputDefinition?
        get() = field as? FieldOutputDefinition
}

interface FieldInputProtocol: FieldProtocol {
    var string: String?
    var value: Any?
    var checked: Boolean?
    var int: Int?
    var float: Float?

    val FieldInputProtocol.fieldInput: FieldInputDefinition?
        get() = field as? FieldInputDefinition
}
