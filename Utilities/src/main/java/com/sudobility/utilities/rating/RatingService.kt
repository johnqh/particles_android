package com.sudobility.utilities.rating


public interface RatingProtocol {
    fun add(points: Int)
}

public class RatingService {
    companion object {
        public var shared: RatingProtocol? = null
    }
}
