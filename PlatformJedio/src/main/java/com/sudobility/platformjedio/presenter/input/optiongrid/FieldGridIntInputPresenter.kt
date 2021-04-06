package com.sudobility.platformjedio.presenter.input.optiongrid

import android.content.Context
import com.sudobility.uitoolkits.shared.UICollectionView


open class FieldGridIntInputPresenter(context: Context): FieldGridInputPresenter(context) {
    private var int: Int?
        get() = this.field?.int
        set(value) {
            this.field?.int = value
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
