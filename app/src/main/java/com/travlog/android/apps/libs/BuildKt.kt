package com.travlog.android.apps.libs

import android.content.pm.PackageInfo
import com.travlog.android.apps.BuildConfig
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class BuildKt(private var packageInfo: PackageInfo) {

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

