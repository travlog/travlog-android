package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.travlog.android.apps.libs.rx.operators.ApiErrorOperator;
import com.travlog.android.apps.libs.rx.operators.Operators;
import com.travlog.android.apps.models.User;
import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.Envelope;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public final class ApiClient implements ApiClientType {

    private final ApiService service;

    public ApiClient(final @NonNull ApiService service) {
        this.service = service;
    }

    @Override
    public Flowable<AccessTokenEnvelope> signUp(final @NonNull XauthBody body) {
        return service.signUp(body)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<AccessTokenEnvelope> signIn(final @NonNull XauthBody body) {
        return service.signIn(body)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<User> updateProfile(final @NonNull User body) {
        return service.updateProfile(body)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map(envelope -> envelope.data);
    }

    @Override
    public Flowable<User> userByUsername(final @NonNull String username) {
        return service.getUserByUsername(username)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map(envelope -> envelope.data);
    }

    /**
     * Utility to create a new {@link ApiErrorOperator}, saves us from littering references to gson throughout the client.
     */
    private @NonNull
    <T extends Envelope> ApiErrorOperator<T> apiErrorOperator() {
        return Operators.apiError(new Gson());
    }
}
