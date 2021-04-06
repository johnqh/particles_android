package com.sudobility.utilities.prompter

import com.sudobility.utilities.Protocols.NSObjectProtocol
import com.sudobility.utilities.kvo.NSObject

public enum class PrompterActionStyle {
    normal,
    cancel,
    destructive
}
public enum class PrompterStyle {
    error,
    selection
}
public typealias PrompterSelection = () -> Unit

public open class PrompterAction: NSObject {
    companion object {

        fun cancel(title: String? = "Cancel", selection: PrompterSelection? = null) : PrompterAction =
            PrompterAction(title = title, style = PrompterActionStyle.cancel, selection = selection)
    }

    var title: String? = null
    var style: PrompterActionStyle = PrompterActionStyle.normal
    var enabled: Boolean = true
    var selection: PrompterSelection? = null

    constructor(title: String?, style: PrompterActionStyle = PrompterActionStyle.normal, enabled: Boolean = true, selection: PrompterSelection? = null) {
        this.title = title
        this.style = style
        this.enabled = enabled
        this.selection = selection
    }
}

public interface PrompterProtocol: NSObjectProtocol {
    var title: String?
    var message: String?
    var style: PrompterStyle
    fun set(title: String?, message: String?, style: PrompterStyle)
    fun prompt(actions: List<PrompterAction>)
    fun dismiss()
}
public typealias TextEntrySelection = (text: String?, ok: Boolean) -> Unit

public interface TextPrompterProtocol: PrompterProtocol {
    var placeholder: String?
    var text: String?
    fun prompt(title: String?, message: String?, text: String?, placeholder: String?, completion: TextEntrySelection)
}

public interface PrompterFactoryProtocol: NSObjectProtocol {
    fun prompter() : PrompterProtocol
    fun textPrompter() : TextPrompterProtocol
}
