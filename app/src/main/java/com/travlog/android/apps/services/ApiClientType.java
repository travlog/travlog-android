package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;

import io.reactivex.Flowable;

public interface ApiClientType {

    Flowable<AccessTokenEnvelope> signup(@NonNull XauthBody body);
}
