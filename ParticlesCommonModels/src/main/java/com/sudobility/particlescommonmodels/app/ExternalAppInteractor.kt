package com.sudobility.particlescommonmodels.app

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.particleskit.interactor.protocol.InteractorProtocol
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.url.URLHandler
import java.net.URL


open class ExternalAppInteractor: NSObject(), InteractorProtocol {
    override var entity: ModelObjectProtocol? = null
    var url: String? = null
    var appId: String? = null

    open fun open() {
        if (url != null) {
            val url = URL(url)
            if (URLHandler.shared?.canOpenURL(url) ?: false) {

            }
        }
    }
}
