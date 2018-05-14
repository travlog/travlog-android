package com.travlog.android.apps.services;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.models.User;
import com.travlog.android.apps.services.apirequests.OauthBody;
import com.travlog.android.apps.services.apirequests.SignInBody;
import com.travlog.android.apps.services.apirequests.SignUpBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.AccountsEnvelope;
import com.travlog.android.apps.services.apiresponses.NoteEnvelope;
import com.travlog.android.apps.services.apiresponses.NotesEnvelope;
import com.travlog.android.apps.services.apiresponses.PostNoteEnvelope;
import com.travlog.android.apps.services.apiresponses.PredictionsEnvelope;
import com.travlog.android.apps.services.apiresponses.ProfileEnvelope;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("signup")
    Flowable<Response<AccessTokenEnvelope>> signUp(@NonNull @Body SignUpBody body);

    @POST("signin")
    Flowable<Response<AccessTokenEnvelope>> signIn(@NonNull @Body SignInBody body);

    @POST("oauth")
    Flowable<Response<AccessTokenEnvelope>> oAuth(@NonNull @Body OauthBody body);

    @PUT("users/{userId}")
    Flowable<Response<ProfileEnvelope>> updateProfile(@NonNull @Path("userId") String userId,
                                                      @NonNull @Body User body);

    @GET("users/{username}")
    Flowable<Response<ProfileEnvelope>> getUserByUsername(@NonNull @Path("username") String username);

    @GET("users/{userId}")
    Flowable<Response<ProfileEnvelope>> getUserByUserId(@NonNull @Path("userId") String userId);

    @PUT("users/{userId}/link")
    Flowable<Response<AccountsEnvelope>> link(@NonNull @Path("userId") String userId,
                                              @NonNull @Body OauthBody body);

    @POST("notes")
    Flowable<Response<PostNoteEnvelope>> postNote(@NonNull @Body Note note);

    @GET("notes")
    Flowable<Response<NotesEnvelope>> notes();

    @GET("notes/{noteId}")
    Flowable<Response<NoteEnvelope>> getNote(@Path("noteId") int noteId);

    @PUT("notes/{noteId}")
    Flowable<Response<NoteEnvelope>> updateNote(@Path("noteId") int noteId, @NonNull @Body Note note);

    @DELETE("notes/{noteId}")
    Flowable<Response<NoteEnvelope>> deleteNote(@Path("noteId") int noteId);

    @GET("maps")
    Flowable<Response<PredictionsEnvelope>> locations(@NonNull @Query("query") String query);
}
