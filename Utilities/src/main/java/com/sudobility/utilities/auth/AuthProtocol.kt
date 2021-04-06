package com.sudobility.utilities.auth

import com.sudobility.utilities.Protocols.NSObjectProtocol

typealias AuthCompletionBlock = (succeeded: Boolean) -> Unit
typealias AuthAttachmentCompletionBlock = (succeeded: Boolean) -> Unit
typealias TokenCompletionBlock = (token: String?, refreshToken: String?, provider: String?) -> Unit

public interface AuthProviderAttachmentProtocol: NSObjectProtocol {
    fun beforeLogin(completion: AuthAttachmentCompletionBlock)
    fun afterLogin()
    fun beforeLogout(token: String, completion: AuthAttachmentCompletionBlock)
    fun afterLogout()
}

public interface AuthProviderProtocol: NSObjectProtocol {
    val token: String?
    val name: String?
    val loginXib: String?
    val logoutXib: String?
    var attachments: MutableList<AuthProviderAttachmentProtocol>?
    fun token(completion: TokenCompletionBlock)
    fun reallyLogin(completion: AuthCompletionBlock)
    fun reallyLogout(completion: AuthCompletionBlock)

    fun add(attachment: AuthProviderAttachmentProtocol) {
        if (attachments == null) {
            attachments = mutableListOf<AuthProviderAttachmentProtocol>()
        }
        attachments?.add(attachment)
    }

    fun login(completion: AuthCompletionBlock) {
        beforeLogin(index = 0) { succeeded  ->
            if (succeeded) {
                this.reallyLogin { successful  ->
                    if (successful) {
                        this.afterLogin()
                    }
                    completion(successful)
                }
            }
        }
    }

    fun attachment(index: Int) : AuthProviderAttachmentProtocol? {
        if (index < attachments?.size ?: 0) {
            return attachments?.get(index)
        }
        return null
    }

    fun beforeLogin(index: Int, completion: AuthAttachmentCompletionBlock) {
        val attachment = this.attachment(index)
        if (attachment != null) {
            attachment.beforeLogin { succeeded  ->
                if (succeeded) {
                    this.beforeLogin(index = index + 1, completion = completion)
                }
            }
        } else {
            completion(true)
        }
    }

    fun afterLogin() {
        val attachments = attachments
        if (attachments != null) {
            for (attachment in attachments) {
                attachment.afterLogin()
            }
        }
    }

    fun logout(token: String, completion: AuthCompletionBlock) {
        beforeLogout(token = token, index = 0) { _  ->
            this.reallyLogout(completion = completion)
        }
    }

    fun beforeLogout(token: String, index: Int, completion: AuthAttachmentCompletionBlock) {
        val attachment = this.attachment(index)
        if (attachment != null) {
            attachment.beforeLogout(token = token) { succeeded  ->
                if (succeeded) {
                    this.beforeLogout(token = token, index = index + 1, completion = completion)
                }
            }
        } else {
            reallyLogout { successful  ->
                if (successful) {
                    this.afterLogout()
                }
                completion(successful)
            }
        }
    }

    fun afterLogout() {
        val attachments = attachments
        if (attachments != null) {
            for (attachment in attachments) {
                attachment.afterLogout()
            }
        }
    }
}
