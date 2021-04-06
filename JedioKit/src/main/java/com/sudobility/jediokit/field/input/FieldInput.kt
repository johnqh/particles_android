package com.sudobility.jediokit.field.input

import com.sudobility.jediokit.field.*
import com.sudobility.jediokit.validator.*
import com.sudobility.particleskit.entity.model.DirtyProtocol
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.extensions.components
import com.sudobility.utilities.extensions.joined
import com.sudobility.utilities.kvo.NSObject


public class FieldInput: NSObject(), FieldInputProtocol {
    companion object {
        private var emailValicator = { EmailFieldValidator() }()
        private var phoneValicator = { PhoneFieldValidator() }()
        private var passwordValidator = { PasswordFieldValidator() }()
        private var nullValidator = { NullFieldValidator() }()
    }

    public override var field: FieldDefinition? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (field !== oldValue) {
                setDefault()
            }
        }
    public override var entity: ModelObjectProtocol? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (entity !== oldValue) {
                setDefault()
            }
        }

    public val fieldName: String?
        get() = parser.asString(fieldInput?.field?.get("field"))

    public val options: List<Map<String, Any>>?
        get() = fieldInput?.options

    public override var value: Any?
        get() {
            val fieldName = fieldName
            val entity = entity as? NSObject
            if (fieldName != null && entity != null) {
                return parser.asString(entity.value(fieldName))
            }
            return null
        }
        set(value) {
            val fieldName = fieldName
            if (fieldName != null) {
                (entity as? NSObject)?.set(value, fieldName)
            }
            (entity as? DirtyProtocol)?.dirty = true
        }

    public override var string: String?
        get() = parser.asString(value)
        set(newValue) {
            value = newValue
        }

    public override var checked: Boolean?
        get() {
            if (fieldInput?.fieldType == FieldType.bool) {
                return bool(fieldInput?.field)
            }
            return null
        }
        set(newValue) {
            if (fieldInput?.fieldType == FieldType.bool) {
                val newValue = newValue
                if (newValue != null) {
                    val option = fieldInput?.option(label = if (newValue) "yes" else "no")
                    if (option != null) {
                        value = option["value"]
                    }
                } else {
                    value = null
                }
            }
        }
    public override var int: Int?
        get() {
            if (fieldInput?.fieldType == FieldType.int) {
                return parser.asInt(value)
            }
            return null
        }
        set(newValue) {
            if (fieldInput?.fieldType == FieldType.int) {
                value = newValue
            }
        }
    public override var float: Float?
        get() {
            if (fieldInput?.fieldType == FieldType.float) {
                return parser.asFloat(value)
            }
            return null
        }
        set(newValue) {
            if (fieldInput?.fieldType == FieldType.float) {
                value = newValue
            }
        }

    public var percent: Float?
        get() {
            if (fieldInput?.fieldType == FieldType.percent) {
                return parser.asFloat(value)
            }
            return null
        }
        set(newValue) {
            if (fieldInput?.fieldType == FieldType.percent) {
                value = newValue
            }
        }

    public var strings: List<String>?
        get() = string?.components(separatedBy = ",")
        set(newValue) {
            value = newValue?.joined(separator = ",")
        }

    private val validator: FieldValidatorProtocol?
        get() {
            return when (fieldInput?.fieldType) {
                FieldType.text -> {
                when (fieldInput?.validator) {
                    FieldValidateType.email -> return emailValicator
                    FieldValidateType.password -> return passwordValidator
                    FieldValidateType.phone -> return phoneValicator
                    else -> nullValidator
                }
            }
                else -> nullValidator
            }
        }

    fun validate() : Error? {
        if (field?.definition("field") != null) {
            val isOptional = fieldInput?.isOptional ?: false
            val fieldName = field?.title?.get("text") as? String ?: field?.subtext?.get("text") as? String
            return validator?.validate(field = fieldName, data = string, optional = isOptional)
        }
        return null
    }

    private fun setDefault() {
        val defaultValue = fieldInput?.defaultValue
        if (defaultValue != null && value == null) {
            value = defaultValue
        }
    }
}

internal fun FieldInput.routingRequest() : RoutingRequest? {
    val link = field?.link
    val urlString = parser.asString(link?.get("text"))
    if (urlString != null) {
        return RoutingRequest(urlString)
    }
    return null
}
