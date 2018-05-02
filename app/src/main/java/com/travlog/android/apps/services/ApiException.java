package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.services.apiresponses.Envelope;

/**
 * An exception class wrapping an {@link Envelope}.
 */
public final class ApiException extends ResponseException {
    private final Envelope errorEnvelope;

    public ApiException(final @NonNull Envelope errorEnvelope, final @NonNull retrofit2.Response response) {
        super(response);
        this.errorEnvelope = errorEnvelope;
    }

    public @NonNull
    Envelope errorEnvelope() {
        return this.errorEnvelope;
    }
}
