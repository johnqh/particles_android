package com.sudobility.uitoolkits.image
import com.sudobility.utilities.app.UtilsApplication
import com.sudobility.utilities.files.FileUtils
import kotlin.reflect.full.staticProperties

public object ImageCache {
    private var cache = HashMap<String, Int>()

    public fun imageId(named: String?): Int? {
        named.let {
            var layoutId = cache[named]
            if (layoutId != 0) {
                layoutId = UtilsApplication.shared()?.imageId(named!!)
                layoutId?.let {
                    cache[named!!] = layoutId
                }
            }
            return  layoutId
        }
        return  null
    }
}