package com.sudobility.routingkit.badging

import com.sudobility.utilities.Protocols.NSObjectProtocol
import com.sudobility.utilities.kvo.NSObject


public interface UrlBadgingProtocol: NSObjectProtocol {
    fun badge(url: String, value: String?)
    fun badge(url: String) : String?
}

public class UrlBadgingProvider: NSObject() {
    companion object {
        var shared: UrlBadgingProtocol? = null
    }
}
