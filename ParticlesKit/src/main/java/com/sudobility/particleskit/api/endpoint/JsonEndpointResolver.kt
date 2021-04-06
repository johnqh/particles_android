package com.sudobility.particleskit.api.endpoint

import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.utilities.json.JsonLoader
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.debug.DebugSettings
import com.sudobility.utilities.utils.parser.Parser


open class JsonEndpointResolver: NSObject, EndpointResolverProtocol {
    companion object {
        var parserOverwrite: Parser? = null
    }

    override open val parser: Parser
        get() = JsonEndpointResolver.parserOverwrite ?: super.parser

    private var entity: DictionaryEntity? = null

    override val host: String?
        get() {
            val host = parser.asString(parser.asDictionary(entity?.data)?.get("host"))
            if (host != null) {
                val custom = parser.asString(DebugSettings?.debug?.get(host))
                if (custom != null) {
                    return custom
                } else {
                    return host
                }
            }
            return null
        }

    override fun path(action: String) : String? =
            parser.asString(parser.asDictionary(parser.asDictionary(entity?.data)?.get("path"))?.get(action))

    constructor() : super() {
    }

    constructor(json: String) : super() {
        val destinations = JsonLoader.load(file = json) as? Map<String, Any>
        if (destinations != null) {
            entity = DictionaryEntity()
            entity?.parse(dictionary = destinations)
        }
    }
}
