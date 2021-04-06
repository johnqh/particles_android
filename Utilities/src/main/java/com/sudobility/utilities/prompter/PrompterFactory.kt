package com.sudobility.utilities.prompter

import com.sudobility.utilities.kvo.NSObject

final public class PrompterFactory: NSObject() {
    companion object {
        var shared: PrompterFactoryProtocol? = null
    }
}
