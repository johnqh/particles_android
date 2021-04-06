package com.sudobility.platformjedio.presenter.input.controls

import com.sudobility.platformjedio.presenter.input.FieldInputPresenter
import com.sudobility.uitoolkits.view.UISegmentedControl


open class FieldSegmentsInputPresenter: FieldInputPresenter() {
    open var segments: UISegmentedControl? = null

    override open fun update() {
        super.update()
//        val segments = segments
//        if (segments != null) {
//            segments.removeAllSegments()
//            val options = field?.fieldInput?.options
//            if (options != null) {
//                var selected: Int? = null
//                if (field?.fieldInput?.optional ?: false) {
//                    insertSegment(title = "Any", value = null, index = 0)
//                    selected = 0
//                }
//                for (option in options) {
//                    val text = parser.asString(option["text"])
//                    if (text != null) {
//                        val value = parser.asString(option["value"])
//                        val index = segments.numberOfSegments
//                        insertSegment(title = text, value = option["value"], index = index)
//                        val value = value
//                        if (value != null) {
//                            if (field?.strings?.contains(value) ?: false) {
//                                selected = index
//                            }
//                        } else {
//                            if (field?.string == null) {
//                                selected = index
//                            }
//                        }
//                    }
//                }
//                val selected = selected
//                if (selected != null) {
//                    segments.selectedSegmentIndex = selected
//                }
//            }
//        }
    }

}
