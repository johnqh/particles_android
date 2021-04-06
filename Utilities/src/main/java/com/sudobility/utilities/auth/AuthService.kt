package com.sudobility.utilities.auth

import com.sudobility.utilities.files.FileUtils


public class AuthService {
    companion object {
        public var shared: AuthService = AuthService()
    }
    private var providers: MutableList<AuthProviderProtocol>? = null
    public var name: String? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (name != oldValue) {
                updateProvider()
            }
        }

    public var provider: AuthProviderProtocol? = null

    fun add(provider: AuthProviderProtocol) {
        if (providers == null) {
            providers = mutableListOf<AuthProviderProtocol>()
        }
        providers?.add(provider)
        updateProvider()
    }

    private fun updateProvider() {
        provider = provider(named = name)
    }

    private fun provider(named: String?) : AuthProviderProtocol? {
        val named = named
        if (named != null) {
            return providers?.first{ provider  ->
                provider.name == named
            }
        } else {
            return providers?.firstOrNull()
        }
    }
}
