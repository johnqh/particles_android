package com.sudobility.utilities.permissions

import android.os.Handler
import android.os.Looper
import com.sudobility.utilities.kvo.NSObject
import com.sudobility.utilities.prompter.PrompterAction
import com.sudobility.utilities.prompter.PrompterFactory
import com.sudobility.utilities.prompter.PrompterStyle

public enum class EPrivacyPermission (val rawValue: Int) {
    unknown(0), notDetermined(1), restricted(2), denied(3), authorized(4);

    companion object {
        operator fun invoke(rawValue: Int) = EPrivacyPermission.values().firstOrNull { it.rawValue == rawValue }
    }
}
typealias PermissionStatusCompletionHandler = (authorization: EPrivacyPermission, background: Boolean?) -> Unit

public interface PrivacyPermissionProtocol {
    var authorization: EPrivacyPermission
    val requestTitle: String?
    val requestMessage: String?
    fun currentAuthorizationStatus(completion: PermissionStatusCompletionHandler)
    fun promptToAuthorize()
    fun promptToSettings()
    fun promptWithRestriction()
    fun performWithAuthorization()
}

open class PrivacyPermission: NSObject, PrivacyPermissionProtocol {
    override val requestTitle: String?
        get() = null
    override val requestMessage: String?
        get() = null
    override var authorization: EPrivacyPermission = EPrivacyPermission.unknown
    set(newValue) {
        val oldValue = field
        field = newValue
        if (authorization != oldValue) {
            when (authorization) {
                EPrivacyPermission.notDetermined -> //                    promptToAuthorize()
                {

                }
                EPrivacyPermission.denied -> //                    promptToSettings()
                {

                }
                EPrivacyPermission.restricted -> //                    promptWithRestriction()
                {

                }
                EPrivacyPermission.authorized -> //                    performWithAuthorization()
                {

                }
                else -> {

                }
            }
        }
    }
    open var background: Boolean? = null
//    private var foregroundToken: NotificationToken? = null

     constructor() : super() {
        Handler(Looper.getMainLooper()).post {
            this.refreshStatus()
        }

         /*
        foregroundToken = NotificationCenter.default.observe(notification = UIApplication.willEnterForegroundNotification, do = { _  ->
            this.refreshStatus()
        })

          */
    }

    open fun refreshStatus() {
        currentAuthorizationStatus { authorization, background  ->
            Handler(Looper.getMainLooper()).post {
                if (this.authorization != authorization) {
                    this.authorization = authorization
                }
                if (this.background != background) {
                    this.background = background
                }
            }
        }
    }

    override fun currentAuthorizationStatus(completion: PermissionStatusCompletionHandler) {
        completion(EPrivacyPermission.notDetermined, null)
    }

    override fun promptToAuthorize() {}

    override fun promptWithRestriction() {}

    override fun promptToSettings() {
        /*
        val prompter = PrompterFactory.shared?.prompter()
        if (prompter != null) {
            prompter.title = requestTitle
            prompter.message = requestMessage
            prompter.style = PrompterStyle.selection
            val cancel = PrompterAction.cancel()
            val settings = PrompterAction(title = "Settings") {
                val url = URL(string = UIApplication.openSettingsURLString)
                if (url != null) {
                    URLHandler.shared?.open(url, completionHandler = null)
                } }
            prompter.prompt(listOf(cancel, settings))
        }

         */
    }

    override fun performWithAuthorization() {}
}
