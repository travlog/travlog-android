package com.travlog.android.apps.libs;

import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;

import com.travlog.android.apps.BuildConfig;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public final class Build {

    private final PackageInfo packageInfo;

    public Build(final @NonNull PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public @NonNull
    String applicationId() {
        return this.packageInfo.packageName;
    }

    public DateTime dateTime() {
        return new DateTime(BuildConfig.BUILD_DATE, DateTimeZone.UTC).withZone(DateTimeZone.getDefault());
    }

    /**
     * Returns `true` if the build is compiled in debug mode, `false` otherwise.
     */
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * Returns `true` if the build is compiled in release mode, `false` otherwise.
     */
    public boolean isRelease() {
        return !BuildConfig.DEBUG;
    }

    public String sha() {
        return BuildConfig.GIT_SHA;
    }

    public Integer versionCode() {
        return this.packageInfo.versionCode;
    }

    public String versionName() {
        return this.packageInfo.versionName;
    }
}
