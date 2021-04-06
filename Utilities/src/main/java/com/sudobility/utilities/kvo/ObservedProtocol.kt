package com.sudobility.utilities.kvo

public interface ObservedProtocol {
    public var _kvoController: KVOController?
    public val kvoController: KVOController
        get() {
            if (_kvoController == null) {
                _kvoController = KVOController()
            }
            return _kvoController!!
        }


    public fun didChange(keyPath: String, old: Any?, new: Any?) {
        kvoController.change(keyPath, old, new)
    }
}