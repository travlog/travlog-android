package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.User;
import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.ProfileEnvelope;

import io.reactivex.Flowable;

public interface ApiClientType {

    Flowable<AccessTokenEnvelope> signUp(@NonNull XauthBody body);

    Flowable<AccessTokenEnvelope> signIn(@NonNull XauthBody body);

    Flowable<User> updateProfile(@NonNull String userId, @NonNull User body);

    Flowable<User> userByUsername(@NonNull String username);

    Flowable<ProfileEnvelope> linkAccounts(@NonNull String userId,
                                           @NonNull XauthBody body);
}
