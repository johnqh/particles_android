package com.sudobility.utilities.bridge

import android.os.Looper
import android.support.v4.os.IResultReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration


internal typealias RunBlock = ()->Unit


public class DispatchQueue {
    public class MainQueue() {
        fun async(block: RunBlock) {
            CoroutineScope(Main).launch {
                block.invoke()
            }
        }

        fun asyncAfter(time:Float, block: RunBlock) {
            CoroutineScope(Default).launch {
                delay(timeMillis = (1000 * time).toLong())
                CoroutineScope(Main).launch {
                    block.invoke()
                }
            }
        }
    }

    public class GlobalQuery() {
        fun async(block: RunBlock) {
            CoroutineScope(Default).launch {
                block.invoke()
            }
        }

        fun asyncAfter(time:Float, block: RunBlock) {
            CoroutineScope(Default).launch {
                delay(timeMillis = (1000 * time).toLong())
                block.invoke()
            }
        }
    }

    companion object {
        public var main: MainQueue = MainQueue()
        public var global: GlobalQuery = GlobalQuery()

        public fun global(): GlobalQuery {
            return global
        }

        public fun runInMainThread(function: ()->Unit) {
            val direct = Thread.currentThread().equals( Looper.getMainLooper().getThread() )
            if (direct) {
                function()
            } else {
                DispatchQueue.main.async(function)
            }
        }
    }
}
