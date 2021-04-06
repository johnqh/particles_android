package com.sudobility.platformjedio.presenter.input.textfield


open class FieldTextFieldIntInputPresenter: FieldTextFieldInputPresenter() {
    private var int: Int?
        get() = this.field?.int
        set(newValue) {
            this.field?.int = newValue
        }

    override open fun update() {
        super.update()
        val int = int
        if (int != null) {
            textField?.text = int?.toString()
        } else {
            textField?.text = null
        }
    }

    override open fun save() {
        val intValue = parser.asInt(textField?.text)
        if (intValue != null) {
            if (int != intValue) {
                int = intValue
            }
        } else {
            if (int != null) {
                int = null
            }
        }
    }
}
