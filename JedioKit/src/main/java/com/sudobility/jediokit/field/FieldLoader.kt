package com.sudobility.jediokit.field

import com.sudobility.utilities.json.JsonLoader
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.parser.Parser
import com.sudobility.utilities.utils.parser.featureFlagged


open class FieldLoader: NSObject() {
    var definitionFile: String? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (definitionFile != oldValue) {
                definitionGroups = load()
            }
        }
    var definitionGroups: List<FieldDefinitionGroup>? = null

    open fun load() : List<FieldDefinitionGroup>? {
        if (definitionFile != null) {
            val json = JsonLoader.load(definitionFile!!)
            val jsonDictionary = json as? List<Map<String, Any>>
            if (jsonDictionary != null) {
                var definitions = mutableListOf<FieldDefinitionGroup>()
                for (itemDictionary in jsonDictionary) {
                    val parsed = parser.asDictionary(itemDictionary)
                    if (parsed != null) {
                        val definition = FieldDefinitionGroup()
                        definition.parse(dictionary = parsed)
                        definitions.add(definition)
                    }
                }
                return definitions
            }
        }
        return null
    }
    open override val parser: Parser
        get() = Parser.featureFlagged
}
