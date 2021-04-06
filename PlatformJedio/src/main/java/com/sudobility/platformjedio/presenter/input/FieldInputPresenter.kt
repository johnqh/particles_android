package com.sudobility.platformjedio.presenter.input

import android.widget.ImageView
import com.sudobility.jediokit.field.input.FieldInput
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.presenter.`object`.ObjectPresenter
import com.sudobility.uitoolkits.image.UIImage
import com.sudobility.uitoolkits.image.image
import com.sudobility.uitoolkits.shared.ImageViewProtocol
import com.sudobility.uitoolkits.shared.LabelProtocol
import com.sudobility.utilities.extensions.localized

open class FieldInputPresenter: ObjectPresenter() {
    open val field: FieldInput?
        get() = model as? FieldInput
    var titleLabel: LabelProtocol? = null
    var subtitleLabel: LabelProtocol? = null
    var textLabel: LabelProtocol? = null
    var imageView: ImageView? = null
    override open var model: ModelObjectProtocol? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = field, keyPath = "field") { _, _, _  ->
                this.update()
            }
            changeObservation(from = oldValue, to = field, keyPath = "entity") { _, _, _  ->
                this.entity = this.field?.entity
            }
        }
    open var entity: ModelObjectProtocol? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = entity, keyPath = "data") { _, _, _  ->
                this.update()
            }
        }

    override fun update() {
        titleLabel?.text = field?.title?.localized
        subtitleLabel?.text = field?.subtitle?.localized
        val image = field?.image
        if (image != null) {
            imageView?.image = UIImage.named(image)
        } else {
            imageView?.image = null
        }
    }
}
