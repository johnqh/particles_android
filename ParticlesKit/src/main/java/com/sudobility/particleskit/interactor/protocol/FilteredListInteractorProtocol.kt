package com.sudobility.particleskit.interactor.protocol

import com.sudobility.particleskit.cache.protocols.LikedObjectsProtocol
import com.sudobility.particleskit.entity.common.FilterEntity
import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.utilities.Protocols.NSObjectProtocol


public interface FilteredListInteractorProtocol: NSObjectProtocol {
    var onlyShowLiked: Boolean
    var liked: LikedObjectsProtocol?
    var filters: FilterEntity?
    var filterText: String?
    val data: List<ModelObjectProtocol>?
}
