package com.sudobility.utilities.error

import com.sudobility.utilities.Protocols.NSObjectProtocol
import com.sudobility.utilities.bridge.NSError
import com.sudobility.utilities.kvo.NSObject

typealias ErrorActionHandler = () -> Unit

public class ErrorAction: NSObject {
    var text: String
    var handler: ErrorActionHandler

    constructor(text: String, handler: ErrorActionHandler) : super() {
        this.text = text
        this.handler = handler
    }

    fun action(sender: Any?) {
        handler()
    }
}

public enum class EInfoType (val rawValue: Int) {
    info(0), wait(1), error(2), warning(3), success(4);

    companion object {
        operator fun invoke(rawValue: Int) = EInfoType.values().firstOrNull { it.rawValue == rawValue }
    }
}

public interface ErrorInfoProtocol: NSObjectProtocol {
    fun info(
        title: String?,
        message: String?,
        type: EInfoType?,
        error: Error?,
        time: Double?,
        actions: List<ErrorAction>?
    )

    fun clear()

    fun ErrorInfoProtocol.info(title: String?, message: String?, error: Error?) {
        info(title = title, message = message, type = null, error = error, time = 3.0)
    }

    fun ErrorInfoProtocol.info(title: String?, message: String?, error: Error?, time: Double?) {
        info(
            title = title,
            message = message,
            type = null,
            error = error,
            time = time,
            actions = null
        )
    }

    fun ErrorInfoProtocol.info(
        title: String?,
        message: String?,
        error: Error?,
        actions: List<ErrorAction>?
    ) {
        info(
            title = title,
            message = message,
            type = null,
            error = error,
            time = 3.0,
            actions = actions
        )
    }

    fun ErrorInfoProtocol.info(title: String?, message: String?, type: EInfoType?, error: Error?) {
        info(title = title, message = message, type = type, error = error, time = 3.0)
    }

    fun ErrorInfoProtocol.info(
        title: String?,
        message: String?,
        type: EInfoType?,
        error: Error?,
        time: Double?
    ) {
        info(
            title = title,
            message = message,
            type = type,
            error = error,
            time = time,
            actions = null
        )
    }

    fun ErrorInfoProtocol.info(
        title: String?,
        message: String?,
        type: EInfoType?,
        error: Error?,
        actions: List<ErrorAction>?
    ) {
        info(
            title = title,
            message = message,
            type = type,
            error = error,
            time = 3.0,
            actions = actions
        )
    }

    fun ErrorInfoProtocol.type(type: EInfoType?, error: Error?): EInfoType {
        val type = type
        if (type != null) {
            return type
        } else {
            val error = error
            if (error != null) {
                val nsError = error as NSError
                if (nsError.code != 0) {
                    return EInfoType.error
                } else {
                    return EInfoType.info
                }
            } else {
                return EInfoType.info
            }
        }
    }

    fun ErrorInfoProtocol.message(message: String?, error: Error?): String? {
        val message = message
        if (message != null) {
            return message
        } else {
            val error = error
            if (error != null) {
                val nsError = error as NSError
                var text: String? = null
                val description = nsError.userInfo?.get("description") as? String
                if (description != null) {
                    text = description
                }
                val errorInfo = nsError.userInfo?.get("error") as? Map<String, Any>
                if (errorInfo != null) {
                    text = errorInfo?.get("msg") as? String ?: errorInfo?.get("message") as? String
                }
                return text ?: error.localizedDescription()
            }
            return null
        }
    }
}

public class ErrorInfo {
    companion object {
        var shared: ErrorInfoProtocol? = null
    }
}

public class ErrorInfoSwitcher: NSObject() {
    companion object {

        fun switcher(errorInfo: ErrorInfoProtocol) : ErrorInfoSwitcher {
            val switcher = ErrorInfoSwitcher()
            switcher.errorInfo = errorInfo
            return switcher
        }
    }

    private var errorInfo: ErrorInfoProtocol? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (errorInfo !== oldValue) {
                previous = ErrorInfo.shared
                ErrorInfo.shared = errorInfo
            }
        }
    private var previous: ErrorInfoProtocol? = null

    /*
    deinit {
        val previous = previous
        if (previous != null) {
            ErrorInfo.shared = previous
        }
    }
     */
}
