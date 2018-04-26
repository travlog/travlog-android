package com.travlog.android.apps.libs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public enum ApiEndpoint {
    PRODUCTION("Production", ""),
    STAGING("Staging", ""),
    LOCAL("Local", "http://192.168.0.5:3000"),
    CUSTOM("Custom", null);

    private String name;
    private String url;

    ApiEndpoint(final @NonNull String name, final @Nullable String url) {
        this.name = name;
        this.url = url;
    }

    public @NonNull
    String url() {
        return this.url;
    }

    public @NonNull
    String toString() {
        return this.name;
    }

    public static ApiEndpoint from(final @NonNull String url) {
        for (final ApiEndpoint value : values()) {
            if (value.url != null && value.url.equals(url)) {
                return value;
            }
        }
        final ApiEndpoint endpoint = CUSTOM;
        endpoint.url = url;
        return endpoint;
    }
}