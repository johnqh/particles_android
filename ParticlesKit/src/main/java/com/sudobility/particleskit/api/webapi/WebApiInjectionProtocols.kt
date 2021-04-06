package com.sudobility.particleskit.api.webapi

import com.sudobility.utilities.Protocols.NSObjectProtocol
import okhttp3.Request
import okhttp3.Response

public typealias URLRequest = Request
public typealias URLResponse = Response

public interface WebApiRequestInjectionProtocol: NSObjectProtocol {
    fun inject(request: Request.Builder, verb: HttpVerb, completion: (request: Request.Builder) -> Unit)
    fun cookies(completion: (Map<String, String>?) -> Unit)
}

public interface WebApiResponseInjectionProtocol: NSObjectProtocol {
    fun inject(response: URLResponse?, data: Any?, verb: HttpVerb?)
}
