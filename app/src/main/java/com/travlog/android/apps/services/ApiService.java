package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/users/signup")
    Flowable<Response<AccessTokenEnvelope>> signup(@NonNull @Body XauthBody body);
}
