package com.sudobility.jediokit.validator


public interface FieldValidatorProtocol {
    fun validate(field: String?, data: Any?, optional: Boolean) : Error?
}

public class TextValidationError(): Error() {
}
