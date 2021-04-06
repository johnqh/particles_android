package com.sudobility.utilities.utils.parser

import com.sudobility.utilities.utils.debug.DebugSettings

public class DebugParser: ConditionalParser() {
    override fun conditioned(data: Any?) : Any? {
        var conditions = mutableMapOf<String , String>()
        val debug = DebugSettings.debug
        if (debug != null) {
            for ((key, value) in debug) {
                val string = Parser.standard.asString(value)
                string.let {
                    conditions[key] = string!!
                }
            }
        }
        this.conditions = conditions
        return super.conditioned(data)
    }
}

public val Parser.Companion.debug: Parser
    get() = DebugParser()