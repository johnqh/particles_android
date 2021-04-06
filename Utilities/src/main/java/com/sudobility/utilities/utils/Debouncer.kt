package com.sudobility.utilities.utils

import android.os.Looper
import com.sudobility.utilities.bridge.DispatchQueue
import com.sudobility.utilities.error.Console
import com.sudobility.utilities.kvo.NSObject

typealias DebouncedFunction = () -> Unit

public class Debouncer(public var fifo: Boolean = false): NSObject() {
    var current: DebounceHandler? = null
        set(newValue) {
            val oldValue = field
            field = newValue
            if (current !== oldValue) {
                if (current != null) {
                    previous = current
                }
            }
        }
    internal var previous: DebounceHandler? = null

    fun debounce() : DebounceHandler? {
        if (!fifo || current == null) {
            val debouncer = DebounceHandler(debouncer = this)
            current = debouncer
            return debouncer
        }
        return null
    }

    internal fun run(handler: DebounceHandler?, function: DebouncedFunction) : Boolean {
        if (handler === current) {
            handler?.reallyRun(function)
            return true
        }
        return false
    }
}

public class DebounceHandler: NSObject {
    private var debouncer: Debouncer? = null

    constructor(debouncer: Debouncer) : super() {
        this.debouncer = debouncer
    }

    public fun run(function: DebouncedFunction, delay: Float?, finish: Boolean = true) {
        val backgrounds: List<DebouncedFunction?> = listOf()
        run(backgrounds = backgrounds, final = function, delay = delay)
    }

    internal fun reallyRun(function: DebouncedFunction) {
        function()
    }

    public fun run(background: DebouncedFunction, then: DebouncedFunction? = null, another: DebouncedFunction? = null, final: DebouncedFunction, delay: Float?) {
        val backgrounds: MutableList<DebouncedFunction?> = mutableListOf(background)
        then?.let {
            backgrounds.add { then!! }
        }
        another?.let {
            backgrounds.add { another!! }
        }
        run(backgrounds = backgrounds, final = final, delay = delay)
    }

    public fun run(backgrounds: List<DebouncedFunction?>, final: DebouncedFunction, delay: Float?) {
        val delay = delay
        if (delay != null && delay!! != 0.0F) {
            Console.shared.log("Debouncer: ${delay}")
        }
        val first = backgrounds.firstOrNull()
        if (first != null) {
            var leftOver = backgrounds.toMutableList()
            leftOver.removeFirst()
            val first = first
            if (first != null) {
                DispatchQueue.global().asyncAfter((delay ?: 0.0F) ) {
                    val ran = this.debouncer?.run(handler = this, function = first)
                    if (ran != null && ran!!) {
                        this.run(backgrounds = leftOver, final = final, delay = 0.0F)
                    }
                }
            } else {
                run(backgrounds = leftOver, final = final, delay = delay)
            }
        } else {
            val direct = Thread.currentThread().equals( Looper.getMainLooper().getThread() ) && delay == null
            if (direct) {
                this.debouncer?.run(handler = this, function = final)
                if (this.debouncer?.fifo ?: false) {
                    this.debouncer?.current = null
                }
            } else {
                DispatchQueue.main.asyncAfter((delay ?: 0.0F)) {
                    this.debouncer?.run(handler = this, function = final)
                    if (this.debouncer?.fifo ?: false) {
                        this.debouncer?.current = null
                    }
                }
            }
        }
    }

    public fun cancel() {
        if (debouncer?.current == this) {
            debouncer?.current = null
        }
    }
}
