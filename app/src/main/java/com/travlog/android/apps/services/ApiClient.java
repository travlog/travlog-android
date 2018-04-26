package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.travlog.android.apps.libs.rx.operators.ApiErrorOperator;
import com.travlog.android.apps.libs.rx.operators.Operators;

public final class ApiClient implements ApiClientType {

    private final ApiService service;

    public ApiClient(final @NonNull ApiService service) {
        this.service = service;
    }

    /**
     * Utility to create a new {@link ApiErrorOperator}, saves us from littering references to gson throughout the client.
     */
    private @NonNull
    <T> ApiErrorOperator<T> apiErrorOperator() {
        return Operators.apiError(new Gson());
    }
}
