package com.sudobility.utilities.tracker

public class CompositeTracking: TrackingProtocol {
    override var userInfo: Map<String, String>?
        get() = trackings.firstOrNull()?.userInfo
        set(newValue) {
            for (tracking in trackings) {
                tracking.userInfo = newValue
            }
        }
    override var excluded: Boolean = false
        set(newValue) {
            val oldValue = field
            field = newValue
            if (excluded != oldValue) {
                for (tracking in trackings) {
                    tracking.excluded = excluded
                }
            }
        }
    private var trackings: MutableList<TrackingProtocol> = mutableListOf<TrackingProtocol>()

    fun add(tracking: TrackingProtocol?) {
        val aTracking = tracking
        if (aTracking != null) {
            aTracking.excluded = excluded
            trackings.add(aTracking)
        }
    }

    override fun path(path: String?, data: Map<String, Any>?) {
        for (tracking: TrackingProtocol in trackings) {
            tracking.path(path, data = data)
        }
    }

    override fun path(path: String?, action: String?, data: Map<String, Any>?) {
        for (tracking: TrackingProtocol in trackings) {
            tracking.path(path, action = action, data = data)
        }
    }

    override open fun log(event: String, data: Map<String, Any>?) {
        for (tracking: TrackingProtocol in trackings) {
            tracking.log(event = event, data = data)
        }
    }
}
