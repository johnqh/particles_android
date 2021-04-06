package com.sudobility.platformjedio.presenter.input.textfield

import com.sudobility.utilities.extensions.localized


open class FieldTextFieldTextInputPresenter: FieldTextFieldInputPresenter() {
    open var string: String?
        get() = this.field?.string
        set(newValue) {
            this.field?.string = newValue
        }

    override open fun update() {
        super.update()
        textField?.text = string?.localized
    }

    override open fun save() {
        val string = textField?.text?.trim() as? String
        if (this.string != string) {
            this.string = string
        }
    }
}
