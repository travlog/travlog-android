package com.travlog.android.apps.services

import com.travlog.android.apps.services.apiresponses.Envelope

/**
 * An exception class wrapping an [Envelope].
 */
class ApiException(private val errorEnvelope: Envelope, response: retrofit2.Response<*>) : ResponseException(response) {

    fun errorEnvelope(): Envelope {
        return this.errorEnvelope
    }
}
