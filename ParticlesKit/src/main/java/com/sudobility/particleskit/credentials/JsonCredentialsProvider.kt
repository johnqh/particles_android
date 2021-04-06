package com.sudobility.particleskit.credentials

import com.sudobility.particleskit.api.endpoint.JsonEndpointResolver
import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.utilities.json.JsonLoader
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.parser.Parser


open class JsonCredentialsProvider: NSObject {
    companion object {
        var parserOverwrite: Parser? = null
    }

    override open val parser: Parser
        get() = JsonEndpointResolver.parserOverwrite ?: super.parser
    private var entity: DictionaryEntity? = null

    fun key(action: String) : String? =
            parser.asString(parser.asDictionary(parser.asDictionary(entity?.data)?.get("keys"))?.get(action))

    constructor() : super() {
        val destinations = JsonLoader.load("credentials.json") as? Map<String, Any>
        if (destinations != null) {
            entity = DictionaryEntity()
            entity?.parse(dictionary = destinations)
        }
    }
}
