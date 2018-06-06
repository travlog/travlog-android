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

    fun deleteNote(noteId: String): Flowable<Note>

    fun searchLocation(query: String): Flowable<List<Prediction>>
}
