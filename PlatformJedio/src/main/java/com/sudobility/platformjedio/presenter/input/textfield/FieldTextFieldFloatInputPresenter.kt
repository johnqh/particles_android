package com.sudobility.platformjedio.presenter.input.textfield


open class FieldTextFieldFloatInputPresenter: FieldTextFieldInputPresenter() {
    private var float: Float?
        get() = this.field?.float
        set(newValue) {
            this.field?.float = newValue
        }

    override open fun update() {
        super.update()
        val float = float
        if (float != null) {
            textField?.text = float.toString()
        } else {
            textField?.text = null
        }
    }

    override open fun save() {
        val floatValue = parser.asFloat(textField?.text)
        if (floatValue != null) {
            float = floatValue
        } else {
            float = null
        }
    }
}
