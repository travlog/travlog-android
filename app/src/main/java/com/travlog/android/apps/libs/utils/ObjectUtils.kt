package com.travlog.android.apps.libs.utils

import io.reactivex.functions.Function

fun isNull(obj: Any?): Boolean {
    return obj == null
}

fun isNotNull(obj: Any?): Boolean {
    return obj != null
}

/**
 * Returns the first non-`null` value of its arguments.
 */
fun <T> coalesce(value: T?, theDefault: T): T {
    if (value != null) {
        return value
    }
    return theDefault
}

/**
 * Returns a function `T -> T` that coalesces values with `theDefault`.
 */
fun <T> coalesceWith(theDefault: T): Function<T, T> {
    return Function { value -> coalesce(value, theDefault) }
}
