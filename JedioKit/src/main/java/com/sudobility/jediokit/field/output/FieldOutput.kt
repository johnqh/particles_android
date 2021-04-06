package com.sudobility.jediokit.field.output

import com.sudobility.jediokit.field.FieldDefinition
import com.sudobility.jediokit.field.FieldOutputProtocol
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.routingkit.router.RoutingOriginatorProtocol
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.kvo.NSObject


open class FieldOutput : NSObject(), FieldOutputProtocol, RoutingOriginatorProtocol {
    open override var field: FieldDefinition? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (field !== oldValue) {
                updateChildren()
            }
        }
    open override var entity: ModelObjectProtocol? = null
        set(value) {
            val oldValue = field
            field = value
            if (entity !== oldValue) {
                updateChildren()
            }
        }
    open override val title: String?
        get() = text(fieldOutput?.title)
    open override val subtitle: String?
        get() = text(fieldOutput?.subtitle)
    open override val text: String?
        get() = text(fieldOutput?.text)
    open override val subtext: String?
        get() = text(fieldOutput?.subtext)
    open override val checked: Boolean?
        get() = bool(fieldOutput?.checked)
    open val link: String?
        get() = text(fieldOutput?.link)
    open val strings: List<String>?
        get() = strings(fieldOutput?.strings)
    open val images: List<String>?
        get() = strings(fieldOutput?.images)
    override open var items: List<FieldOutputProtocol>? = null
    override open val hasData: Boolean
        get() {
            if (entity != null && fieldOutput != null) {
                return hasData(fieldOutput?.title) || hasData(fieldOutput?.subtitle) || hasData(fieldOutput?.text) || hasData(fieldOutput?.subtext) || hasData(fieldOutput?.image) || hasData(fieldOutput?.checked) || hasData(fieldOutput?.strings) || hasData(fieldOutput?.images) || hasData(fieldOutput?.link, textOK = true) || (items?.size
                        ?: 0) > 0
            }
            return false
        }

    fun updateChildren() {
        val definitions = fieldOutput?.items
        if (definitions != null) {
            var fields: MutableList<FieldOutput> = mutableListOf<FieldOutput>()
            for (definition in definitions) {
                val field = FieldOutput()
                field.entity = entity
                field.field = definition
                if (field.hasData) {
                    fields.add(field)
                }
            }
            if (fields.size > 0) {
                items = fields
            } else {
                items = null
            }
        } else {
            items = null
        }
    }

    override fun routingRequest(): RoutingRequest? {
        val link = link
        if (link != null) {
            return RoutingRequest(url = link)
        }
        return null
    }
}
