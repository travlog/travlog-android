package com.travlog.android.apps.services

import com.travlog.android.apps.models.Note
import com.travlog.android.apps.models.User
import com.travlog.android.apps.services.apirequests.OauthBody
import com.travlog.android.apps.services.apirequests.SignInBody
import com.travlog.android.apps.services.apirequests.SignUpBody
import com.travlog.android.apps.services.apiresponses.*
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("signup")
    fun signUp(@Body body: SignUpBody): Flowable<Response<AccessTokenEnvelope>>

    @POST("signin")
    fun signIn(@Body body: SignInBody): Flowable<Response<AccessTokenEnvelope>>

    @POST("oauth")
    fun oAuth(@Body body: OauthBody): Flowable<Response<AccessTokenEnvelope>>

    @PUT("users/{userId}")
    fun updateProfile(@Path("userId") userId: String,
                      @Body body: User): Flowable<Response<ProfileEnvelope>>

    @GET("users/{username}")
    fun getUserByUsername(@Path("username") username: String): Flowable<Response<ProfileEnvelope>>

    @GET("users/{userId}")
    fun getUserByUserId(@Path("userId") userId: String): Flowable<Response<ProfileEnvelope>>

    @PUT("users/{userId}/link")
    fun link(@Path("userId") userId: String,
             @Body body: OauthBody): Flowable<Response<AccountsEnvelope>>

    @POST("notes")
    fun postNote(@Body note: Note): Flowable<Response<PostNoteEnvelope>>

    @GET("notes")
    fun notes(): Flowable<Response<NotesEnvelope>>

    @GET("notes/{noteId}")
    fun getNote(@Path("noteId") noteId: String): Flowable<Response<NoteEnvelope>>

    @PUT("notes/{noteId}")
    fun updateNote(@Path("noteId") noteId: String, @Body note: Note): Flowable<Response<NoteEnvelope>>

    @DELETE("notes/{noteId}")
    fun deleteNote(@Path("noteId") noteId: String): Flowable<Response<NoteEnvelope>>

    @GET("maps")
    fun locations(@Query("query") query: String): Flowable<Response<PredictionsEnvelope>>
}
