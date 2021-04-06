package com.sudobility.platformjedio.presenter.input.list

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

open class FieldListTextInputPresenter(context: Context): FieldListInputPresenter(context) {
    private var string: String?
        get() = this.field?.string
        set(newValue) {
            this.field?.string = newValue
        }

    override open fun isSelected(value: Any?) : Boolean =
        string == parser.asString(value)

    override open fun select(value: Any?) {
        string = parser.asString(value)
    }

    override open fun deselect(value: Any?) {
        if (isSelected(value = value)) {
            string = null
        }
    }
}
