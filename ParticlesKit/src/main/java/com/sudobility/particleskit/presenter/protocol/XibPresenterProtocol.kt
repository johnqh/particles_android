package com.sudobility.particleskit.presenter.protocol

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.presenter.XibPresenterCache
import com.sudobility.utilities.Protocols.NSObjectProtocol


public interface XibPresenterProtocol: NSObjectProtocol {
    var xibCache: XibPresenterCache

    fun XibPresenterProtocol.xib(obj: ModelObjectProtocol?): Int? =
        xibCache.layoutId(obj)
}