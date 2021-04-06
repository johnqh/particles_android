package com.sudobility.utilities.rating

import com.sudobility.utilities.bridge.UserDefaults
import com.sudobility.utilities.kvo.NSObject


open class PointsRating: NSObject, RatingProtocol {
    override fun add(points: Int) {
        this.points = this.points + points
    }
    private val pointsKey: String
        get() = "${className}.points"

    private var threshold: Int
    open var points: Int
        get() = UserDefaults.standard.integer(forKey = pointsKey)
        set(newValue) {
            if (newValue >= threshold) {
                promptForRating()
                UserDefaults.standard.set(0, forKey = pointsKey)
            } else {
                UserDefaults.standard.set(newValue, forKey = pointsKey)
            }
        }

    constructor(threshold: Int) : super() {
        this.threshold = threshold
    }

    open fun promptForRating() {}
}
