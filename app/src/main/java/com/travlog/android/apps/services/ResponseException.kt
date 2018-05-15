package com.travlog.android.apps.services

/**
 * An exception class wrapping a [retrofit2.Response].
 */
open class ResponseException(private val response: retrofit2.Response<*>) : RuntimeException() {

    fun response(): retrofit2.Response<*> {
        return this.response
    }
}
