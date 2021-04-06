package com.sudobility.particleskit.presenter
import com.sudobility.particleskit.app.ParticlesApplication
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import kotlin.reflect.full.staticProperties

public class XibPresenterCache {
    private var cache = HashMap<String, Int>()

    public fun parse(obj: Any) {
        val objClass = obj.javaClass.kotlin
        objClass.staticProperties.forEach { property ->
            val value = property.get() as? Int
            value?.let {
                cache[property.name] = value
            }
        }
    }

    public fun layoutId(obj: ModelObjectProtocol?): Int? {
        return layoutId(obj?.className)
    }

    public fun layoutId(named: String?): Int? {
        named.let {
            var layoutId = cache[named]
            if (layoutId != 0) {
                layoutId = ParticlesApplication.shared()?.layoutId(named!!)
                layoutId?.let {
                    cache[named!!] = layoutId
                }
            }
            return  layoutId
        }
        return  null
    }
}