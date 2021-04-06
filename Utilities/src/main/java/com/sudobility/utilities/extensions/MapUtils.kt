package com.sudobility.utilities.extensions

public object MapUtils {
    public fun merge(map1: Map<String, Any>?, map2: Map<String, Any>?): Map<String, Any>? {
        map1.let { map1 ->
            map2.let { map2 ->
                return map1!!.toMutableMap().apply {
                    map2!!.forEach { key, value ->
                        merge(key, value) { currentValue, addedValue ->
                            "$currentValue, $addedValue" // just concatenate... no duplicates-check..
                        }
                    }
                }
            }
            return map1
        }
        map2.let {
            return map2
        }
        return null
    }
}

public fun <K, V> MutableMap<K, V>.merge(another: Map<K, V>, choose: (value1: V, value2: V)->V ) {
    apply {
        another.forEach { key, value ->
            merge(key, value) { currentValue, addedValue ->
                choose(currentValue, addedValue)
            }
        }
    }
}