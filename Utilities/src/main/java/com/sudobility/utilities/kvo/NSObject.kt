package com.sudobility.utilities.kvo

import com.sudobility.utilities.Protocols.NSObjectProtocol
import com.sudobility.utilities.utils.parser.Parser
import java.util.*
import kotlin.collections.HashMap

open class NSObject : NSObjectProtocol, ObservedProtocol, ObservingProtocol {
    override var _uuid: UUID? = null
    override val parser: Parser
        get() = Parser.standard

    override var _kvoController: KVOController? = null

    private var properties: MutableMap<String, Any?> = mutableMapOf()
    private var changes: MutableMap<String, Any?> = mutableMapOf()

    override fun value(key: String): Any? {
        return  properties[key]
    }

    override fun set(value: Any?, key: String) {
        willChangeValue(key)
        properties[key] = value
        didChangeValue(key)
    }

    override val className: String
        get() = javaClass.simpleName


    public override fun willChangeValue(forKey:String) {
        val value = value(forKey)
        changes[forKey] = value
    }

    public override fun didChangeValue(forKey:String) {
        val value = value(forKey)
        val oldValue = changes[forKey]
        didChange(forKey, oldValue, value)
    }
}