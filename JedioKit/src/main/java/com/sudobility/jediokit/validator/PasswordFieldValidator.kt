package com.sudobility.jediokit.validator

import com.sudobility.utilities.bridge.NSError
import com.sudobility.utilities.kvo.NSObject

public class PasswordFieldValidator: NSObject(), FieldValidatorProtocol {
    override fun validate(field: String?, data: Any?, optional: Boolean) : Error? {
        if (data != null) {
            return null
        } else {
            if (optional) {
                return null
            } else {
                val className = this.className
                val field = field?.toLowerCase()
                if (field != null) {
                    return NSError(domain = "${className}.data.missing", code = 0, userInfo = mapOf("message" to "Please enter ${field}."))
                } else {
                    return NSError(domain = "${className}.data.missing", code = 0, userInfo = mapOf("message" to "Please enter required data."))
                }
            }
        }
    }
}
