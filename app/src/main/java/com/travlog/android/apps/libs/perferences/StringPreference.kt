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

package com.travlog.android.apps.libs.perferences

import android.content.SharedPreferences

class StringPreference(private val sharedPreferences: SharedPreferences,
                       private val key: String,
                       private val defaultValue: String? = null) : StringPreferenceType {

    override fun get(): String? {
        return this.sharedPreferences.getString(this.key, this.defaultValue)
    }

    override fun isSet(): Boolean {
        return this.sharedPreferences.contains(this.key)
    }

    override fun set(value: String) {
        this.sharedPreferences.edit().putString(this.key, value).apply()
    }

    override fun delete() {
        this.sharedPreferences.edit().remove(this.key).apply()
    }
}