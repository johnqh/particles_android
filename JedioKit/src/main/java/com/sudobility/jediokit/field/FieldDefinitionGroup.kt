package com.sudobility.jediokit.field

import com.sudobility.jediokit.field.input.FieldInput
import com.sudobility.jediokit.field.output.FieldOutput
import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.presenter.protocol.XibProviderProtocol
import com.sudobility.utilities.extensions.components
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.parser.Parser
import com.sudobility.utilities.utils.parser.featureFlagged


open class FieldDefinitionGroup: DictionaryEntity() {
    override open val parser: Parser
        get() = Parser.featureFlagged
    val input: Boolean
        get() = parser.asBoolean(data?.get("input")) ?: false
    val title: String?
        get() = parser.asString(data?.get("title"))
    var definitions: List<FieldDefinition>? = null

    fun definition(field: String) : Map<String, Any>? =
            data?.get(field) as? Map<String, Any>

    override open fun parse(dictionary: Map<String, Any>) {
        super.parse(dictionary = dictionary)
        val definitionsData = parser.asArray(data?.get("fields")) as? List<Map<String, Any>>
        if (definitionsData != null) {
            var definitions = mutableListOf<FieldDefinition>()
            for (itemDictionary in definitionsData) {
                val definition = fieldDefinition()
                definition.parse(dictionary = itemDictionary)
                if (definition.visible) {
                    definitions.add(definition)
                }
            }
            this.definitions = definitions
        }
    }

    open fun fieldDefinition() : FieldDefinition =
            if (input) FieldInputDefinition() else FieldOutputDefinition()

    open fun field(entity: ModelObjectProtocol) : FieldProtocol? =
            if (input) FieldInput() else FieldOutput()

    open fun transformToFieldData(entity: ModelObjectProtocol) : List<FieldProtocol>? {
        val definitions = this.definitions
        if (definitions != null) {
            var fields = mutableListOf<FieldProtocol>()
            for (definition in definitions) {
                val field = field(entity = entity)
                if (field != null) {
                    field.field = definition
                    field.entity = entity as? ModelObjectProtocol
                    fields.add(field)
                    val items = (field as? FieldOutputProtocol)?.items
                    if (items != null) {
                        for (item in items) {
                            fields.add(item)
                        }
                    }
                }
            }
            return fields
        }
        return null
    }

    open fun shouldShow(entity: ModelObjectProtocol, field: FieldProtocol) : Boolean {
        var shouldShow = true
        val dependencies = field.dependencies
        if (dependencies != null) {
            for (arg0 in dependencies) {
                if (shouldShow) {
                    val (key, value) = arg0
                    val string = parser.asString(value)
                    val valueString = parser.asString((entity as? NSObject)?.value(key))
                    if (string != null) {
                        val valueStrings = valueString?.components(separatedBy = ",")
                        shouldShow = valueStrings?.contains(string) ?: false
                    } else {
                        shouldShow = valueString == null
                    }
                }
            }
        }
        if (shouldShow) {
            if (input) {
                return true
            } else {
                val output = field as? FieldOutputProtocol
                if (output != null) {
                    if ((output as? XibProviderProtocol)?.xib != null) {
                        return true
                    }
                    return output.hasData
                }
                return false
            }
        } else {
            return false
        }
    }
}
