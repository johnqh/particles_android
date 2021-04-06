package com.sudobility.particlescommonmodels.like

import com.sudobility.particleskit.cache.protocols.LikedObjectsProtocol
import com.sudobility.particleskit.entity.entity.DictionaryEntity
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.localdata.LocalEntityCacheInteractor
import com.sudobility.particleskit.loader.LoaderProtocol
import com.sudobility.particleskit.loader.LoaderProvider


open class LikedKeysInteractor: LocalEntityCacheInteractor(), LikedObjectsProtocol {
    private val likedTag = "liked"
    private val dislikedTag = "unliked"

    override var liked: List<String>?
        get() = (entity as? DictionaryEntity)?.force?.json?.get(likedTag) as? List<String>
    set(value) {
        willChangeValue(likedTag)
        if (value != null) {
            (entity as? DictionaryEntity)?.force?.data?.set(likedTag, value)
        } else {
            (entity as? DictionaryEntity)?.force?.data?.remove(likedTag)
        }
        didChangeValue(likedTag)
    }

    override var disliked: List<String>?
        get() = (entity as? DictionaryEntity)?.force?.json?.get(likedTag) as? List<String>
        set(value) {
            willChangeValue(dislikedTag)
            if (value != null) {
                (entity as? DictionaryEntity)?.force?.data?.set(dislikedTag, value)
            } else {
                (entity as? DictionaryEntity)?.force?.data?.remove(dislikedTag)
            }
            didChangeValue(dislikedTag)
        }

    open override fun createLoader() : LoaderProtocol? {
        val path = path
        if (path != null) {
            return LoaderProvider.shared?.localAsyncLoader(path = path, cache = this)
        }
        return null
    }

    open override fun entityObject() : ModelObjectProtocol {
        val entity = DictionaryEntity().force
        entity.data?.set(likedTag, listOf<String>())
        entity.data?.set(dislikedTag, listOf<String>())
        return entity
    }

    override fun addLike(key: String?) {
        removeDislike(key = key)
        val key = key
        val liked = liked
        if (key != null && liked != null) {
            if (liked.indexOf(key) == null) {
                var modified = liked.toMutableList()
                modified.add(key)
                this.liked = modified
            }
        }
    }

    override fun removeLike(key: String?) {
        val key = key
        val liked = liked
        if (key != null && liked != null) {
            val index = liked.indexOf(key)
            if (index != null) {
                var modified = liked.toMutableList()
                modified.removeAt(index)
                this.liked = modified
            }
        }
    }

    override fun toggleLike(key: String?) {
        val key = key
        val liked = liked
        if (key != null && liked != null) {
            var modified = liked.toMutableList()
            val index = liked.indexOf(key)
            if (index != null) {
                modified.removeAt(index)
            } else {
                removeDislike(key = key)
                modified.add(key)
            }
            this.liked = modified
        }
    }

    override fun liked(key: String?) : Boolean {
        val key = key
        val liked = liked
        if (key != null && liked != null) {
            return liked.indexOf(key) != null
        }
        return false
    }

    override fun addDislike(key: String?) {
        removeLike(key = key)
        val key = key
        val disliked = disliked
        if (key != null && disliked != null) {
            if (disliked.indexOf(key) == null) {
                var modified = disliked.toMutableList()
                modified.add(key)
                this.disliked = modified
            }
        }
    }

    override fun removeDislike(key: String?) {
        val key = key
        val disliked = disliked
        if (key != null && disliked != null) {
            val index = disliked.indexOf(key)
            if (index != null) {
                var modified = disliked.toMutableList()
                modified.removeAt(index)
                this.disliked = modified
            }
        }
    }

    override fun toggleDislike(key: String?) {
        val key = key
        val disliked = disliked
        if (key != null && disliked != null) {
            var modified = disliked.toMutableList()
            val index = disliked.indexOf(key)
            if (index != null) {
                modified.removeAt(index)
            } else {
                removeLike(key = key)
                modified.add(key)
            }
            this.disliked = modified
        }
    }

    override fun disliked(key: String?) : Boolean {
        val key = key
        val disliked = disliked
        if (key != null && disliked != null) {
            return disliked.indexOf(key) != null
        }
        return false
    }
}
