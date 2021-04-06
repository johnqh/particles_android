package com.sudobility.uitoolkits.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.widget.ImageView
import com.sudobility.utilities.app.UtilsApplication
import com.sudobility.utilities.files.FileUtils

public object UIImage {
    public fun named(image: String): Drawable? {
        var imageId = ImageCache.imageId(image)
        imageId?.let {
            var drawable: Drawable? = null
            try {
                drawable = UtilsApplication.shared().getDrawable(imageId!!)
            } finally {
                return drawable
            }
        }
        return null
    }
}

public var ImageView.image: Drawable?
    get() = this.drawable
    set(value) {
        setImageDrawable(value)
    }