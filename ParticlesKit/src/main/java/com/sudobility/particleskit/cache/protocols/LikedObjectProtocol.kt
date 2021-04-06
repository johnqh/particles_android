package com.sudobility.particleskit.cache.protocols

import com.sudobility.utilities.Protocols.NSObjectProtocol

public interface LikedObjectsProtocol: NSObjectProtocol {
    var liked: List<String>?
    var disliked: List<String>?
    fun addLike(key: String?)
    fun removeLike(key: String?)
    fun toggleLike(key: String?)
    fun liked(key: String?) : Boolean
    fun addDislike(key: String?)
    fun removeDislike(key: String?)
    fun toggleDislike(key: String?)
    fun disliked(key: String?) : Boolean
}
