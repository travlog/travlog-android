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

import com.travlog.android.apps.models.Account
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.models.User
import com.travlog.android.apps.services.apirequests.OauthBody
import com.travlog.android.apps.services.apirequests.SignInBody
import com.travlog.android.apps.services.apirequests.SignUpBody
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope

import io.reactivex.Flowable

interface ApiClientType {

    fun signUp(body: SignUpBody): Flowable<AccessTokenEnvelope>

    fun signIn(body: SignInBody): Flowable<AccessTokenEnvelope>

    fun oAuth(body: OauthBody): Flowable<AccessTokenEnvelope>

    fun updateProfile(userId: String, body: User): Flowable<User>

    fun userByUsername(username: String): Flowable<User>

    fun linkAccounts(userId: String,
                     body: OauthBody): Flowable<List<Account>>

    fun postNote(note: Note): Flowable<Note>

    fun notes(): Flowable<List<Note>>

    fun getNote(noteId: String): Flowable<Note>

    fun updateNote(note: Note): Flowable<Note>

    fun deleteNote(noteId: String): Flowable<String>

    fun searchLocation(query: String): Flowable<List<Prediction>>
}
