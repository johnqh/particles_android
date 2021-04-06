package com.sudobility.platformjedio.presenter.input.controls

import android.widget.ToggleButton
import com.sudobility.platformjedio.presenter.input.FieldInputPresenter

open class FieldSwitchInputPresenter: FieldInputPresenter() {
    open var checkbox: ToggleButton? = null

    private var checked: Boolean?
        get() = this.field?.checked
        set(newValue) {
            this.field?.checked = newValue
        }

    override open fun update() {
        super.update()
        checkbox?.isChecked = field?.checked ?: false
    }

    fun check() {
        field?.checked = checkbox?.isChecked
    }
}
