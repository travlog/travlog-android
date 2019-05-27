/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
