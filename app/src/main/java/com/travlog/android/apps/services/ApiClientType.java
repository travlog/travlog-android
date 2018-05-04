package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.User;
import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.CheckExistEnvelope;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Path;

public interface ApiClientType {

    Flowable<AccessTokenEnvelope> signUp(@NonNull XauthBody body);

    Flowable<AccessTokenEnvelope> signIn(@NonNull XauthBody body);

    Flowable<User> updateProfile(@NonNull User body);

    Flowable<User> userByUsername(@NonNull String username);
}
