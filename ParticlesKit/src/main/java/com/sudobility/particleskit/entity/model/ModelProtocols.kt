package com.sudobility.particleskit.entity.model

import android.location.Location
import com.sudobility.routingkit.router.RoutingOriginatorProtocol
import com.sudobility.utilities.Protocols.NSObjectProtocol
import java.util.*


public interface ModelObjectProtocol: NSObjectProtocol, RoutingOriginatorProtocol {
    var parent: ModelObjectProtocol?
        get() = null
        set(value) {}
    var index: Int
        get() = 0
        set(value) {}

    val key: String? get() = null
    val displayTitle: String? get() = null
    val displaySubtitle: String?  get() = null
    val displayImageUrl: String?  get() = null
    var isSelected: Boolean
        get() = false
        set(value) {}

    fun children(tag: String?) : List<ModelObjectProtocol>? {
        return null
    }
    fun order(another: ModelObjectProtocol?) : Boolean {
        return false
    }
}

public interface FilterableProtocol: NSObjectProtocol {
    fun filter(lowercased: String?) : Boolean
}

public interface ClusteredModelObjectProtocol: ModelObjectProtocol {
    val cluster: List<ModelObjectProtocol>?
}

public interface DirtyProtocol: NSObjectProtocol {
    var dirty_time: Date?
    var dirty: Boolean
}

public  interface ModelListProtocol: ModelObjectProtocol {
    var list: List<ModelObjectProtocol>?
}

public  interface ModelGridProtocol: ModelObjectProtocol {
    var grid: List<List<ModelObjectProtocol>>?
    val width: Int
    val height: Int
}

public interface DateModelObjectProtocol: ModelObjectProtocol {
    val date: Date?
}

public interface JsonPersistable {
    var json: Map<String, Any>?
    val thinned: Map<String, Any>?
}

public interface LocalCacheProtocol: NSObjectProtocol {
    fun entity(data: Map<String, Any>?) : ModelObjectProtocol?
}

public  enum class AnnotationInclusion (val rawValue: Int) {
    none(0), ifNone(1), always(2);

    companion object {
        operator fun invoke(rawValue: Int) = AnnotationInclusion.values().firstOrNull { it.rawValue == rawValue }
    }
}

// do not zoom to annotation
// only zoom to annotation if there are no annotation previously
// always zoom to annotation
public interface AnnotationProtocol: NSObjectProtocol {
    val annotationCoordinate: Location
    val annotationTitle: String?
    val annotationSubtitle: String?
    val inclusion: AnnotationInclusion
    val preferedContent: Boolean
}
