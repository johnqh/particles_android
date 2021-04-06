package com.sudobility.platformjedio.presenter.input.textfield

import android.widget.TextView
import com.sudobility.platformjedio.presenter.input.FieldInputPresenter

open class FieldTextFieldInputPresenter: FieldInputPresenter() {
    var textField: TextView? = null

    override open fun update() {
        super.update()
    }

    open fun save() {}
}
