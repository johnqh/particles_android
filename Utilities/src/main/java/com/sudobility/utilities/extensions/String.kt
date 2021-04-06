package com.sudobility.utilities.extensions

import java.net.URLEncoder

public fun String.components(separatedBy: String): List<String> {
    return this.split(separatedBy, ignoreCase = true)
}

public fun List<String>.joined(separator: String): String {
    return  this.joinToString(separator)
}

public val String.localized: String
    get() = this

public fun String.replacingOccurrences(of: String, with: String) = this.replace(of, with)

public fun String.lowercased() = this.toLowerCase()

public fun String.encodeUrl() = URLEncoder.encode(this, "utf-8")

public val String.extension: String
    get() = this.substringAfterLast('.', "")

public fun String.stringByAppendingPathComponent(path: String): String {
    return "${this}/${path}"
}