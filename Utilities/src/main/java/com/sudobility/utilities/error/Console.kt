package com.sudobility.utilities.error

import com.sudobility.utilities.BuildConfig
import com.sudobility.utilities.files.FileUtils
import com.sudobility.utilities.utils.debug.Installation

public class Console {
    companion object {
        public var shared: Console = Console()
    }

    private val visible: Boolean
        get() {
            if (BuildConfig.DEBUG) {
                return true
            } else {
                if (Installation.appStore) {
                    return false
                }
                return true
            }
        }


    public fun log(obj: Any?) {
        if (visible && obj != null) {
            print(obj)
        }
    }

    public fun log(object1: Any?, object2: Any?) {
        log("${object1 ?: ""}\n${object2 ?: ""}")
    }
}
