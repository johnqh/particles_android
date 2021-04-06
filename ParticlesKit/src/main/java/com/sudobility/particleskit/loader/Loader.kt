package com.sudobility.particleskit.loader

import com.sudobility.particleskit.api.ApiProtocol
import com.sudobility.particleskit.cache.IOProtocol
import com.sudobility.particleskit.entity.model.JsonPersistable
import com.sudobility.particleskit.entity.model.LocalCacheProtocol
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.utilities.Protocols.ParsingProtocol
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.Debouncer
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaClass
import kotlin.reflect.jvm.internal.impl.load.kotlin.KotlinClassFinder
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

open class Loader(internal var type:String, internal var path: String, internal var io: List<IOProtocol>, internal var fields: List<String>? = null, internal var cache: LocalCacheProtocol? = null): NSObject(), LoaderProtocol {
    internal var loadTime: Date? = null
    internal var lastPriority: Int? = null
    internal var readerDebounce: Debouncer = Debouncer()
    internal var writerDebounce: Debouncer = Debouncer()

    override fun load(params: Map<String, Any>?, completion: LoaderCompletionHandler?) {
        val handler = readerDebounce.debounce()
        if (handler != null) {
            handler.run({
                this.lastPriority = null
                for (i in 0 until this.io.size) {
                    val ioLoader = this.io[i]
                    ioLoader.priority = i
                    ioLoader.load(path = this.path, params = params) { data, _, priority, error  ->
                        handler.run({
                            val lastPriority = this.lastPriority ?: -1
                            if (priority > lastPriority) {
                                this.lastPriority = priority
                                val data = data
                                if (data != null) {
                                    this.parse(data = data, error = error, completion = completion)
                                } else {
                                    completion?.invoke(data, null, error)
                                }
                            }
                        }, delay = null)
                    }
                }
            }, delay = null)
        }
    }

    override fun parse(data: Any?, error: Error?, completion: LoaderCompletionHandler?) {
        val entities = parseEntities(data = data)
        if (entities != null) {
            completion?.invoke(entities, loadTime, error)
        }  else {
            val entity = parseEntity(data = data)
            if (entity != null) {
                completion?.invoke(entity, loadTime, error)
            } else {
                completion?.invoke(null, loadTime, error)
            }
        }
    }

    private fun parseEntities(data: Any?) : List<ModelObjectProtocol>? {
        val result = result(data = data)
        if (result != null) {
            val array = result as? List<Any>
            if (array != null) {
                var entities = mutableListOf<ModelObjectProtocol>()
                for (entityData in array) {
                    val entity = entity(data = entityData)
                    if (entity != null) {
                        entities.add(entity)
                    }
                }
                return entities
            }
        }
        return null
    }

    open fun result(data: Any?) : Any? =
        data

    private fun parseEntity(data: Any?) : ModelObjectProtocol? {
        val result = result(data = data)
        if (result != null) {
            return entity(data = result)
        }
        return null
    }

    open fun entity(data: Any?) : ModelObjectProtocol? {
        val entityData = data as? Map<String, Any>
        if (entityData != null) {
            val obj = entity(data = entityData)
            (obj as? ParsingProtocol)?.parse(entityData)
            return obj
        }
        return null
    }

    open fun entity(data: Map<String, Any>?) : ModelObjectProtocol? {
        val entity = cache?.entity(data)
        if (entity != null) {
            return entity
        }
        return createEntity()
    }

    override fun createEntity() : ModelObjectProtocol? {
        return Class.forName(type).newInstance() as? ModelObjectProtocol
    }

    override fun save(obj: Any?) {
        if (lastPriority == io.size - 1) {
            val entity = obj as? JsonPersistable
            val data = entity?.json
            if (entity != null && data != null) {
                saveData(data = data)
            } else {
                val entities = obj as? List<JsonPersistable>
                if (entities != null) {
                    var data = mutableListOf< Map<String, Any> > ()
                    for (entity in entities) {
                        val entityData = entity.json
                        if (entityData != null) {
                            data.add(entityData)
                        }
                    }
                    saveData(data = data)
                } else {
                    val entities = obj as? Map<String, JsonPersistable>
                    if (entities != null) {
                        var data = mutableListOf< Map<String, Any> > ()
                        for ((_, entity) in entities) {
                            val entityData = entity.json
                            if (entityData != null) {
                                data.add(entityData)
                            }
                        }
                        saveData(data = data)
                    }
                }
            }
        }
    }

    fun saveData(data: Any?) {
        val handler = writerDebounce.debounce()
        if (handler != null) {
            handler.run({
                for (i in 0 until this.io.size) {
                    val ioLoader = this.io[i]
                    if (!(ioLoader is ApiProtocol)) {
                        ioLoader.save(path = this.path, params = null, data = data, completion = null)
                    }
                }
            }, delay = null)
        }
    }
}
