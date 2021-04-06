package com.sudobility.particlescommonmodels.browse

import com.sudobility.particleskit.cache.protocols.SavedSearchesProtocol
import com.sudobility.particleskit.entity.common.SavedSearchEntity
import com.sudobility.particleskit.interactor.list.DataListInteractor


open class LocalSavedSearchCacheInteractor: DataListInteractor(), SavedSearchesProtocol {
    override var savedSearches: List<SavedSearchEntity>?
        get() = data as? List<SavedSearchEntity>
        set(newValue) {
            data = newValue
        }

    override fun add(name: String, search: String?, filters: Map<String, Any>?) {
        var savedSearches = this.savedSearches?.toMutableList() ?: mutableListOf<SavedSearchEntity>()
        val savedSearch = SavedSearchEntity()
        savedSearch.name = name
        savedSearch.text = search
        savedSearch.filters = filters
        savedSearches.add(savedSearch)
        this.savedSearches = savedSearches
        loader?.save(data)
    }

    override fun remove(savedSearch: SavedSearchEntity) {
        var savedSearches = this.savedSearches?.toMutableList()
        val index = savedSearches?.indexOfFirst{ item  ->
            item === savedSearch
        }
        if (index != null) {
            savedSearches?.removeAt(index)
        }
        this.savedSearches = savedSearches
    }
}
