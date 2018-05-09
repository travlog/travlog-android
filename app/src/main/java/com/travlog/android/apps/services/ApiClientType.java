package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Account;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.models.User;
import com.travlog.android.apps.services.apirequests.OauthBody;
import com.travlog.android.apps.services.apirequests.SignInBody;
import com.travlog.android.apps.services.apirequests.SignUpBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;

import java.util.List;

import io.reactivex.Flowable;

public interface ApiClientType {

    Flowable<AccessTokenEnvelope> signUp(@NonNull SignUpBody body);

    Flowable<AccessTokenEnvelope> signIn(@NonNull SignInBody body);

    Flowable<AccessTokenEnvelope> oAuth(@NonNull OauthBody body);

    Flowable<User> updateProfile(@NonNull String userId, @NonNull User body);

    Flowable<User> userByUsername(@NonNull String username);

    Flowable<List<Account>> linkAccounts(@NonNull String userId,
                                         @NonNull OauthBody body);

    Flowable<Note> postNote(@NonNull Note note);
}
