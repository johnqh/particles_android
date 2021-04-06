package com.sudobility.particleskit.loader

import com.sudobility.particleskit.entity.model.LocalCacheProtocol

public interface LoaderProviderProtocol {
    fun loader(tag: String, cache: LocalCacheProtocol?) : LoaderProtocol?
    fun localLoader(path: String, cache: LocalCacheProtocol?) : LoaderProtocol?
    fun localAsyncLoader(path: String, cache: LocalCacheProtocol?) : LoaderProtocol?
}

public class LoaderProvider {
    companion object {
        public var shared: LoaderProviderProtocol? = null
    }
}
