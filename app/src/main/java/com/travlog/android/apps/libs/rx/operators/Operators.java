package com.travlog.android.apps.libs.rx.operators;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.travlog.android.apps.services.ApiException;
import com.travlog.android.apps.services.ResponseException;

import org.reactivestreams.Subscriber;

public final class Operators {

    private Operators() {
    }

    /**
     * When a response errors, send an {@link ApiException} or {@link ResponseException} to
     * {@link Subscriber#onError}, otherwise send the response to {@link Subscriber#onNext}.
     */
    public static @NonNull
    <T> ApiErrorOperator<T> apiError(final @NonNull Gson gson) {
        return new ApiErrorOperator<>(gson);
    }
}
