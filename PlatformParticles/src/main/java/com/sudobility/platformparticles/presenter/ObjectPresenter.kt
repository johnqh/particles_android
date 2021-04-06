package com.sudobility.platformparticles.presenter

import android.content.Context
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.sudobility.particleskit.entity.model.ModelObjectProtocol

open class ObjectPresenter {
    public var context: Context? = null
    public var view: ViewGroup? = null
        set(value) {
            field = value
            getSubviews()
            update()
        }
    public var model: ModelObjectProtocol? = null
        set(value) {
            field = value
            update()
        }

    open fun getSubviews() {
    }

    open fun update() {
        if (view != null && model != null) {
            updateSelf()
        }
    }

    open fun updateSelf() {

    }
}