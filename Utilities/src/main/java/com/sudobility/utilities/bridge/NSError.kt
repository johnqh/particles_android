package com.sudobility.utilities.bridge

public fun Error.localizedDescription(): String {
    return "Error"
}

public class NSError(): Error() {
    public var domain: String? = null
    public var code: Int = 0
    public var userInfo: Map<String, Any>? = null

    public constructor(domain: String, code: Int, userInfo: Map<String, Any>?) : this() {
        this.domain = domain
        this.code = code
        this.userInfo = userInfo
    }

    public constructor(code: Int, userInfo: Map<String, Any>?) : this() {
        this.code = code
        this.userInfo = userInfo
    }
    public fun localizedDescription(): String {
        return message ?: "Error: ${code}"
    }
}