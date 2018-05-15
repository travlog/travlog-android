package com.travlog.android.apps.services.apiresponses

import com.travlog.android.apps.services.ApiException

open class Envelope {

    var code: String = ""
    var codeno = 0
    var success = false
    var err = Error()

    val isDuplicatedUser: Boolean
        get() = codeno == DUPLICATED_USER

    inner class Error {
        var msg = ""
    }

    companion object {

        val DUPLICATED_USER = 4009

        /**
         * Tries to extract an [Envelope] from an exception, and if it
         * can't returns null.
         */
        fun fromThrowable(t: Throwable): Envelope? {
            return if (t is ApiException) {
                t.errorEnvelope()
            } else null

        }
    }
}
