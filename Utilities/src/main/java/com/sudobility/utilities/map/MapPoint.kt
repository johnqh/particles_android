package com.sudobility.utilities.map

import com.sudobility.utilities.kvo.NSObject

public class MapPoint: NSObject {
    public var latitude: Double? = null
    public var longitude: Double? = null

    constructor(latitude: Double? = null, longitude: Double? = null) {
        this.latitude = latitude
        this.longitude = longitude
    }
}
