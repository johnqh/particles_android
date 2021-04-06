package com.sudobility.particleskit.cache.protocols

import com.sudobility.particleskit.entity.common.SavedSearchEntity
import com.sudobility.utilities.Protocols.NSObjectProtocol

public interface SavedSearchesProtocol: NSObjectProtocol {
    var savedSearches: List<SavedSearchEntity>?
    fun add(name: String, search: String?, filters: Map<String, Any>?)
    fun remove(savedSearch: SavedSearchEntity)
}
