package com.sudobility.platformjedio.presenter.input.list

import android.content.Context

open class FieldListStringsInputPresenter(context: Context): FieldListInputPresenter(context) {
    override open fun isSelected(value: Any?) : Boolean {
        val string = parser.asString(value)
        if (string != null) {
            return field?.strings?.contains(string) ?: false
        }
        return false
    }

    override open fun select(value: Any?) {
        val string = parser.asString(value)
        if (string != null) {
            var strings = field?.strings?.toMutableList() ?: mutableListOf<String>()
            if (!strings.contains(string)) {
                strings.add(string)
                field?.strings = strings
            }
        }
    }

    override open fun deselect(value: Any?) {
        val string = parser.asString(value)
        if (string != null) {
            var strings = field?.strings?.toMutableList() ?: mutableListOf<String>()
            val index = strings.indexOf() {string }
            if (index != null) {
                strings.removeAt(index)
                field?.strings = strings
            }
        }
    }
}
