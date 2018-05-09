package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.travlog.android.apps.libs.rx.operators.ApiErrorOperator;
import com.travlog.android.apps.libs.rx.operators.Operators;
import com.travlog.android.apps.models.Account;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.models.User;
import com.travlog.android.apps.services.apirequests.OauthBody;
import com.travlog.android.apps.services.apirequests.SignInBody;
import com.travlog.android.apps.services.apirequests.SignUpBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.Envelope;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public final class ApiClient implements ApiClientType {

    private final ApiService service;

    public ApiClient(final @NonNull ApiService service) {
        this.service = service;
    }

    @Override
    public Flowable<AccessTokenEnvelope> signUp(final @NonNull SignUpBody body) {
        return service.signUp(body)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<AccessTokenEnvelope> signIn(final @NonNull SignInBody body) {
        return service.signIn(body)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<AccessTokenEnvelope> oAuth(final @NonNull OauthBody body) {
        return service.oAuth(body)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<User> updateProfile(final @NonNull String userId, final @NonNull User body) {
        return service.updateProfile(userId, body)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map(envelope -> envelope.data.user);
    }

    @Override
    public Flowable<User> userByUsername(final @NonNull String username) {
        return service.getUserByUsername(username)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map(envelope -> envelope.data.user);
    }

    @Override
    public Flowable<List<Account>> linkAccounts(final @NonNull String userId,
                                                final @NonNull OauthBody body) {

        return service.link(userId, body)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map(envelope -> envelope.data.accounts);
    }

    @Override
    public Flowable<Note> postNote(final @NonNull Note note) {
        return service.postNote(note)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map(envelope -> envelope.data);
    }

    @Override
    public Flowable<List<Note>> notes() {
        return service.notes()
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map(envelope -> envelope.data.notes);
    }

    /**
     * Utility to create a new {@link ApiErrorOperator}, saves us from littering references to gson throughout the client.
     */
    private @NonNull
    <T extends Envelope> ApiErrorOperator<T> apiErrorOperator() {
        return Operators.apiError(new Gson());
    }
}
