package com.sudobility.platformparticles.presenter

import android.widget.RelativeLayout
import android.content.Context
import android.util.AttributeSet
import com.sudobility.particleskit.entity.model.ModelObjectProtocol


class ObjectPresenterView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {
    public var model: ModelObjectProtocol? = null
        set(value) {
            field = value
            presenter?.model = model
        }

    public var presenter: ObjectPresenter?  = null
        set(value) {
            presenter?.model = null
            presenter?.view = null
            presenter?.context = null
            field = value
            presenter?.context = context
            presenter?.view = this
            presenter?.model = model
        }

    // override all constructors to ensure custom logic runs in all cases
    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0) {}

    init {
        // put all custom logic in this constructor, which always runs
    }

    public fun inflate(layoutId: Int) {
        inflate(getContext(), layoutId, this)
    }
}