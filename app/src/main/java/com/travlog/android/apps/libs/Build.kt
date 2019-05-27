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

import android.content.pm.PackageInfo
import com.travlog.android.apps.BuildConfig
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class Build(private var packageInfo: PackageInfo) {

    fun applicationId(): String {
        return this.packageInfo.packageName
    }

    fun dateTime(): DateTime {
        return DateTime(BuildConfig.BUILD_DATE, DateTimeZone.UTC).withZone(DateTimeZone.getDefault())
    }

    /**
     * Returns `true` if the build is compiled in debug mode, `false` otherwise.
     */
    fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    /**
     * Returns `true` if the build is compiled in release mode, `false` otherwise.
     */
    fun isRelease(): Boolean {
        return !BuildConfig.DEBUG
    }

    fun sha(): String {
        return BuildConfig.GIT_SHA
    }

    fun versionCode(): Int {
        return this.packageInfo.versionCode
    }

    fun versionName(): String {
        return this.packageInfo.versionName
    }
}

