package com.travlog.android.apps.services

import com.travlog.android.apps.models.Note
import com.travlog.android.apps.models.User
import com.travlog.android.apps.services.apirequests.OauthBody
import com.travlog.android.apps.services.apirequests.SignInBody
import com.travlog.android.apps.services.apirequests.SignUpBody
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope
import com.travlog.android.apps.services.apiresponses.AccountsEnvelope
import com.travlog.android.apps.services.apiresponses.NoteEnvelope
import com.travlog.android.apps.services.apiresponses.NotesEnvelope
import com.travlog.android.apps.services.apiresponses.PostNoteEnvelope
import com.travlog.android.apps.services.apiresponses.PredictionsEnvelope
import com.travlog.android.apps.services.apiresponses.ProfileEnvelope

import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
    fun getNote(@Path("noteId") noteId: Int): Flowable<Response<NoteEnvelope>>

    @PUT("notes/{noteId}")
    fun updateNote(@Path("noteId") noteId: Int, @Body note: Note): Flowable<Response<NoteEnvelope>>

    @DELETE("notes/{noteId}")
    fun deleteNote(@Path("noteId") noteId: Int): Flowable<Response<NoteEnvelope>>

    @GET("maps")
    fun locations(@Query("query") query: String): Flowable<Response<PredictionsEnvelope>>
}
