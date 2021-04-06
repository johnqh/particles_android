package com.sudobility.particleskit.common

import com.sudobility.particleskit.entity.model.ModelObjectProtocol
import com.sudobility.routingkit.router.RoutingRequest
import com.sudobility.utilities.kvo.NSObject


final class AppInfo : NSObject, ModelObjectProtocol {
    companion object {
        public var shared: AppInfo = AppInfo()
    }

    var name: String? = null
    var version: String? = null
    val fullName: String?
        get() {
            val name = name
            if (name != null) {
                val version = version
                if (version != null) {
                    return "${name} v${version}"
                } else {
                    return name
                }
            } else {
                return null
            }
        }

    constructor() : super() {
//        name = (Bundle.main.infoDictionary?["CFBundleDisplayName"] as? String) ?: (Bundle.main.infoDictionary?["CFBundleName"] as? String)
//        val version = Bundle.main.version
//        if (version != null) {
//            val build = Bundle.main.build
//            if (build != null) {
//                this.version = "${version}.${build}"
//            } else {
//                this.version = "${version}"
//            }
//        }
    }
}
