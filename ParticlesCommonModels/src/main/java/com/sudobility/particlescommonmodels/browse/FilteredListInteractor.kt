package com.sudobility.particlescommonmodels.browse

import com.sudobility.particleskit.cache.protocols.LikedObjectsProtocol
import com.sudobility.particleskit.entity.common.FilterEntity
import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.list.ListInteractor
import com.sudobility.particleskit.interactor.protocol.FilteredListInteractorProtocol
import com.sudobility.utilities.bridge.DispatchQueue
import com.sudobility.utilities.extensions.components
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.utils.Debouncer
import com.sudobility.utilities.utils.LoadingStatus

// interactor is responsible for loading/transforming/assembling data

open class FilteredListInteractor: ListInteractor(), FilteredListInteractorProtocol {
    private var debouncer: Debouncer = Debouncer()
    open override var loading: Boolean = false
        set(newValue) {
            val oldValue = field
            field = newValue
            if (loading != oldValue) {
                status = if (loading) LoadingStatus.shared else null
            }
        }
    private var status: LoadingStatus? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (status !== oldValue) {}
        }
    //                oldValue?.minus()
    //                status?.plus()
    override var onlyShowLiked: Boolean = false
        set(newValue) {
            val oldValue = field
            field = newValue
            if (onlyShowLiked != oldValue) {
                filter()
            }
        }
    override var liked: LikedObjectsProtocol? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = liked, keyPath = "liked") { _, _, _  ->
                this.likedChanged()
            }
            changeObservation(from = oldValue, to = liked, keyPath = "disliked") { _, _, _  ->
                this.filter()
            }
        }
    override var filters: FilterEntity? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            changeObservation(from = oldValue, to = filters, keyPath = "data") { _, _, _  ->
                this.filter()
            }
        }
    override val data: List<ModelObjectProtocol>?
        get() = null

    override var filterText: String? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            filter()
        }

    open val filterFields: List<String>?
        get() = null


    open fun likedChanged() {
        if (onlyShowLiked) {
            filter()
        }
    }

    open fun filter() {
        val data = data
        if (data != null) {
            val handler = debouncer.debounce()
            if (handler != null) {
                handler.run({
                    this.loading = true
                    DispatchQueue.global().async {
                        val filterText = this.filterText?.toLowerCase()
                        val filtered = data.filter({ item  ->
                            val key = item.key
                            if (key != null) {
                                val key = key
                                if (key != null) {
                                    if (this.onlyShowLiked) {
                                        if (this.liked?.liked?.contains(key) ?: false) {
                                            return@filter this.filter(data = item, text = filterText, filters = this.filters?.data)
                                        }
                                    } else if (this.liked?.disliked?.contains(key) ?: false) {
                                        return@filter false
                                    } else {
                                        return@filter this.filter(data = item, text = filterText, filters = this.filters?.data)
                                    }
                                }
                            }
                            false
                        })
                        val sorted = this.sort(data = filtered)
                        DispatchQueue.main.async {
                            handler.run({
                                this.sync(sorted)
                                this.loading = false
                            }, delay = null)
                        }
                    }
                }, delay = 0.5F)
            }
        } else {
            val handler = debouncer.debounce()
            if (handler != null) {
                handler.run({
                    this.sync(null)
                }, delay = 0.0F)
            }
        }
    }

    open fun filter(data: ModelObjectProtocol, text: String?, filters: Map<String, Any>?) : Boolean =
        filter(data = data, text = text) && filter(data = data, filters = filters)

    open fun filter(data: ModelObjectProtocol, text: String?) : Boolean {
        val lowercased = text?.trim()?.toLowerCase()
        val dictionary = (data as? DictionaryEntity)?.force?.data
        if (lowercased != null && dictionary != null) {
            val filterFields = filterFields
            if (filterFields != null) {
                val first = filterFields.first { filterField  ->
                    val data = data as? NSObject
                    val string = parser.asString(data?.value(filterField))?.toLowerCase()
                    if (data != null && string != null) {
                        return string.contains(lowercased)
                    }
                    false
                }
                if (first != null) {
                    return true
                }
            } else {
                val first = dictionary.values.first { value  ->
                    val string = (value as? String)?.toLowerCase()
                    if (string != null) {
                        return string.contains(lowercased)
                    }
                    false
                }
                if (first != null) {
                    return true
                }
            }
            return false
        }
        return true
    }

    open fun filter(data: ModelObjectProtocol, filters: Map<String, Any>?) : Boolean {
        val filters = filters
        if (filters != null) {
            var ok = true
            for ((key, value) in filters) {
                val lowercased = (value as? String)?.toLowerCase()?.components(separatedBy = ",")
                if (!filter(data = data, key = key, value = lowercased ?: value)) {
                    ok = false
                    break
                }
            }
            return ok
        }
        return true
    }

    open fun filter(data: ModelObjectProtocol, key: String, value: Any) : Boolean {
        val strings = value as? List<String>
        if (strings != null) {
            val value = parser.asString(data.value(key))
            if (value != null) {
                return strings.first { string  ->
                    value.contains(string)
                } != null
            }
        }

        val string = value as? String
        if (string != null) {
            val value = parser.asString(data.value(key))
            if (value != null) {
                return value.contains(string)
            }
        }

        val int = value as? Int
        if (int != null) {
            val value = parser.asInt(data.value(key))
            if (value != null) {
                return value == int
            }
        }

        val float = value as? Float
        if (float != null) {
            val value = parser.asFloat(data.value(key))
            if (value != null) {
                return value == float
            }
        }

        val bool = value as? Boolean
        if (bool != null) {
            val value = parser.asBoolean(data.value(key))
            if (value != null) {
                return value == bool
            }
        }
        return false
    }

    open fun sort(data: List<ModelObjectProtocol>?) : List<ModelObjectProtocol>? =
        data
}
