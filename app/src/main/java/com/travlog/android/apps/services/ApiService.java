package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.User;
import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.ProfileEnvelope;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("signup")
    Flowable<Response<AccessTokenEnvelope>> signUp(@NonNull @Body XauthBody body);

    @POST("signin")
    Flowable<Response<AccessTokenEnvelope>> signIn(@NonNull @Body XauthBody body);

    @PUT("users/{userId}")
    Flowable<Response<ProfileEnvelope>> updateProfile(@NonNull @Path("userId") String userId,
                                                      @NonNull @Body User body);

    @GET("users/{username}")
    Flowable<Response<ProfileEnvelope>> getUserByUsername(@NonNull @Path("username") String username);

    @GET("users/{userId}")
    Flowable<Response<ProfileEnvelope>> getUserByUserId(@NonNull @Path("userId") String userId);

    @PUT("users/{userId}/link")
    Flowable<Response<ProfileEnvelope>> link(@NonNull @Path("userId") String userId,
                                             @NonNull @Body XauthBody body);
}
