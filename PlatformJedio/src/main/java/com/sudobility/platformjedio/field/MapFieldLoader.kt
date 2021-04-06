package com.sudobility.platformjedio.field

import com.sudobility.jediokit.field.FieldDefinitionGroup
import com.sudobility.jediokit.field.FieldInputDefinition
import com.sudobility.jediokit.field.FieldLoader
import java.net.URL

open class MapFieldLoader: FieldLoader() {
    open override fun load() : List<FieldDefinitionGroup>? {
        val groups = super.load()
        if (groups != null) {
            for (group in groups) {
                val definitions = group.definitions
                if (definitions != null) {
                    for (definition in definitions) {
                        val inputDefinition = definition as? FieldInputDefinition
                        val options = inputDefinition?.options
                        if (inputDefinition != null && options != null) {
                            var modified = listOf<Map<String, Any>>()
                            for (option in options) {
                                val urlString = option["value"] as? String
                                if (urlString != null) {
                                    val url = URL(urlString!!)
//                                    if (urlString != null && url != null && URLHandler.shared?.canOpenURL(
//                                            url
//                                        ) ?: false
//                                    ) {
//                                        modified.append(option)
//                                    }
                                }
                            }
                            inputDefinition.options = modified
                        }
                    }
                }
            }
            return groups
        }
        return null
    }
}
