package com.travlog.android.apps.libs.rx

import java.util.NoSuchElementException

class Optional<M>(private val optional: M?) {

    val isNotEmpty: Boolean
        get() = this.optional != null

    val isEmpty: Boolean
        get() = this.optional == null

    fun get(): M {
        if (optional == null) {
            throw NoSuchElementException("No value present")
        }
        return optional
    }
}
