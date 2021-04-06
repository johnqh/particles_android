package com.sudobility.particleskit.loader

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import java.util.*

public typealias LoaderCompletionHandler = (loadedObject: Any?, loadTime: Date?, error: Error?) -> Unit

interface LoaderProtocol {
    fun load(params: Map<String, Any>?, completion: LoaderCompletionHandler?)
    fun parse(data: Any?, error: Error?, completion: LoaderCompletionHandler?)
    fun save(obj: Any?)
    fun createEntity() : ModelObjectProtocol?
}
