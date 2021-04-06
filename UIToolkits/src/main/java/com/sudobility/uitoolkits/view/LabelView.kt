package com.sudobility.uitoolkits.view

import android.content.Context
import android.view.View
import android.widget.TextView
import com.sudobility.uitoolkits.shared.*
import java.text.AttributedString


open class LabelView: View {
    var textColor: Int?
        get() {
            return label?.currentTextColor
        }
        set(value) {
            if (value != null) {
                label?.setTextColor(value!!)
            }
        }

    var font: NativeFont?
        get() = label?.font
        set(value) {
            label?.font = value
        }

    var label: TextView? = null
    var sublabel: TextView? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (sublabel !== oldValue) {
                if (subtext != null) {
                    sublabel?.text = subtext
                } else {
                    subtext = sublabel?.text as String?
                }
            }
        }

    var text: String?
        get() {
            return label?.text as String?
        }
        set(value) {
            label?.text = value
            updateVisibility()
        }

    open var subtext: String? = null
        set(value) {
            val oldValue = field
            field = value
            if (subtext != oldValue) {
                sublabel?.text = subtext
            }
        }

    constructor(context: Context): super(context)

    open fun updateVisibility() {
        visible = (label?.text?.trim() != null)
    }
}
