package com.sudobility.particleskit.api.endpoint

public interface EndpointResolverProtocol {
    val host: String?
    fun path(action: String) : String?
}
