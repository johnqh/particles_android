package com.sudobility.particlescommonmodels.doer

import com.sudobility.utilities.kvo.NSObject


public interface DoerProtocol {
    fun perform() : Boolean
    fun undo()
}

public class Doer: NSObject() {
    companion object {
        var shared: Doer = { Doer() }()
    }

    private var doers: MutableList<DoerProtocol> = mutableListOf<DoerProtocol>()

    fun perform(doer: DoerProtocol) : Boolean {
        if (doer.perform()) {
            doers.add(doer)
            return true
        } else {
            return false
        }
    }

    fun undo() {
        val doer = doers.lastOrNull()
        if (doer != null) {
            doer.undo()
            doers.removeLast()
        }
    }
}
