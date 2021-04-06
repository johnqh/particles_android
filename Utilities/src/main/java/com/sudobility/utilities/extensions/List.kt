package com.sudobility.utilities.extensions

public fun <E> MutableList<E>.append(element: E) {
    this.add(element)
}

public fun <E> MutableList<E>.remove(at: Int) {
    this.removeAt(at)
}

public fun <T> List<T>.sorted(by: (value1: T, value2: T) -> Int): List<T> {
    val comparator = Comparator(by)
    return this.sortedWith(comparator)
}

public fun <E, T> List<E>.compactMap(function: (E)-> T?): List<T> {
    return this.mapNotNull {
        function(it)
    }
}