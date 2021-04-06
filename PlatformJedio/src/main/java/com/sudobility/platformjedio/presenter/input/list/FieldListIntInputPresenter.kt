package com.sudobility.platformjedio.presenter.input.list

import android.content.Context
import androidx.recyclerview.widget.RecyclerView


open class FieldListIntInputPresenter(context: Context): FieldListInputPresenter(context) {
    private var int: Int?
        get() = this.field?.int
        set(newValue) {
            this.field?.int = newValue
        }

    override open fun isSelected(value: Any?) : Boolean =
        int == parser.asInt(value)

    override open fun select(value: Any?) {
        int = parser.asInt(value)
    }

    override open fun deselect(value: Any?) {
        if (isSelected(value = value)) {
            int = null
        }
    }
}
