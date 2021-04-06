package com.sudobility.utilities.Protocols

import com.sudobility.utilities.kvo.ObservedProtocol
import com.sudobility.utilities.kvo.ObservingProtocol
import com.sudobility.utilities.utils.parser.Parser

public interface NSObjectProtocol: ObservedProtocol, ObservingProtocol {
    val className: String
    val parser: Parser

    fun value(key: String): Any?
    fun set(value: Any?, key: String)
    fun willChangeValue(forKey:String)
    fun didChangeValue(forKey:String)
}