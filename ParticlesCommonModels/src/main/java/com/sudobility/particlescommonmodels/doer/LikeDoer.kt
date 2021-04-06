package com.sudobility.particlescommonmodels.doer

import com.sudobility.particlescommonmodels.like.LikedKeysInteractor

public enum class LikeDoerAction {
    like,
    dislike
}

public class LikeDoer: DoerProtocol {
    var likedManager: LikedKeysInteractor? = null
    var key: String? = null
    var action: LikeDoerAction
    var liked: Boolean = false
    var disliked: Boolean = false

    constructor(action: LikeDoerAction, likedManager: LikedKeysInteractor?, key: String?) {
        this.action = action
        this.likedManager = likedManager
        this.key = key
    }

    override fun perform() : Boolean {
        val likedManager = likedManager
        val key = key
        if (likedManager != null && key != null) {
            liked = likedManager.liked(key = key)
            disliked = likedManager.disliked(key = key)
            when (action) {
                LikeDoerAction.like -> {
                    likedManager.toggleLike(key = key)
                    return true
                }
                LikeDoerAction.dislike -> {
                    likedManager.toggleDislike(key = key)
                    return true
                }
            }
        }
        return false
    }

    override fun undo() {
        if (liked) {
            likedManager?.addLike(key = key)
        } else {
            likedManager?.removeLike(key = key)
        }
        if (disliked) {
            likedManager?.addDislike(key = key)
        } else {
            likedManager?.removeDislike(key = key)
        }
    }
}
