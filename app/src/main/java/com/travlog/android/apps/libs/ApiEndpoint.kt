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

package com.travlog.android.apps.libs

enum class ApiEndpoint(private val type: String, private var url: String) {
    PRODUCTION("Production", ""),
    STAGING("Staging", "http://13.125.111.118:3000/api/"),
    LOCAL("Local", "http://192.168.0.5:3000/api/"),
    CUSTOM("Custom", "");

    fun url(): String {
        return this.url
    }

    override fun toString(): String {
        return this.type
    }

    companion object {

        fun from(url: String): ApiEndpoint {
            for (value in values()) {
                if (value.url == url) {
                    return value
                }
            }
            val endpoint = CUSTOM
            endpoint.url = url
            return endpoint
        }
    }
}