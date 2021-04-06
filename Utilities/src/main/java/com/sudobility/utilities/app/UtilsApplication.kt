package com.sudobility.utilities.app

import android.app.Application
import android.content.Context
import com.sudobility.utilities.utils.debug.Installation

open class UtilsApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: UtilsApplication? = null

        fun shared(): UtilsApplication {
            return instance!!
        }
    }

    public val context: Context
        get() = getApplicationContext()

    override fun onCreate() {
        super.onCreate()
        Installation.context = context
    }

    public fun imageId(image: String): Int {
        return resources.getIdentifier(image, "drawable", packageName)
    }
}