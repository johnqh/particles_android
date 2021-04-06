package com.sudobility.uitoolkits.view

import android.content.Context
import android.view.View
import com.sudobility.uitoolkits.color.ColorPalette
import com.sudobility.uitoolkits.shared.*


open class TextCollectionViewCell: UICollectionViewCell {
    var unselectedBackgroundColor: String? = null
    var selectedBackgroundColor: String? = null
    var unselectedTextColor: String? = null
    var selectedTextColor: String? = null
    public var view: View? = null
        set(value) {
            val oldValue = field
            field = value
            updateTextColor()
            updateSelected()
        }

    var textLabel: LabelProtocol? = null
        set(value) {
            val oldValue = field
            field = value
            this.isSelected = true
            updateTextColor()
            updateSelected()
        }

    var text: String?
        get() = textLabel?.text
        set(newValue) {
            textLabel?.text = newValue
        }

    public constructor(context: Context) : super(context) {

    }

    open fun updateTextColor() {
        textLabel?.textColor = view?.borderColor ?: UIColor.black
    }

    open fun updateSelected() {
        if (isSelected) {
            val selectedTextColor = selectedTextColor
            if (selectedTextColor != null) {
                textLabel?.textColor = ColorPalette.shared.colorSystem(selectedTextColor) ?: UIColor.black
            } else {
                view?.backgroundColor = textLabel?.textColor ?: UIColor.clear
            }
            val selectedBackgroundColor = selectedBackgroundColor
            if (selectedBackgroundColor != null) {
                view?.backgroundColor = ColorPalette.shared.colorSystem(selectedBackgroundColor)
            } else {
                textLabel?.textColor = view?.borderColor ?: UIColor.black
            }
        } else {
            val unselectedTextColor = unselectedTextColor
            if (unselectedTextColor != null) {
                textLabel?.textColor = ColorPalette.shared.colorSystem(unselectedTextColor) ?: UIColor.black
            } else {
                textLabel?.textColor = view?.backgroundColor ?: UIColor.clear
            }
            val unselectedBackgroundColor = unselectedBackgroundColor
            if (unselectedBackgroundColor != null) {
                view?.backgroundColor = ColorPalette.shared.colorSystem(unselectedBackgroundColor)
            } else {
                view?.backgroundColor = view?.borderColor
            }
        }
    }
}
