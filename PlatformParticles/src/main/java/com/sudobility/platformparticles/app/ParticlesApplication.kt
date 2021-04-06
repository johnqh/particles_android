package com.sudobility.platformparticles.app
import android.app.Application
import android.content.res.Configuration
import com.sudobility.platformparticles.cache.LayoutCache
import com.sudobility.platformparticles.xib.XibLoader
import com.sudobility.utilities.app.UtilsApplication

open class ParticlesApplication : UtilsApplication() {
    companion object {
        fun shared() : ParticlesApplication? {
            return UtilsApplication.shared() as? ParticlesApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    public fun layoutId(layout: String): Int {
        return resources.getIdentifier(layout, "layout", packageName)
    }
}