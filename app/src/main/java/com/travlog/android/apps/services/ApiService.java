package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/signup")
    Flowable<Response<AccessTokenEnvelope>> signUp(@NonNull @Body XauthBody body);

    @POST("/signin")
    Flowable<Response<AccessTokenEnvelope>> signIn(@NonNull @Body XauthBody body);
}
