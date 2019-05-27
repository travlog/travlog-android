/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
