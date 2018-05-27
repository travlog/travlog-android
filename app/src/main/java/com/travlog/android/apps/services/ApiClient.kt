package com.travlog.android.apps.services

import com.google.gson.Gson
import com.travlog.android.apps.libs.rx.operators.ApiErrorOperator
import com.travlog.android.apps.libs.rx.operators.Operators
import com.travlog.android.apps.models.Account
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.models.User
import com.travlog.android.apps.services.apirequests.OauthBody
import com.travlog.android.apps.services.apirequests.SignInBody
import com.travlog.android.apps.services.apirequests.SignUpBody
import com.travlog.android.apps.services.apiresponses.*
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class ApiClient(private val service: ApiService) : ApiClientType {

    override fun signUp(body: SignUpBody): Flowable<AccessTokenEnvelope> {
        return service.signUp(body)
                .lift<AccessTokenEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
    }

    override fun signIn(body: SignInBody): Flowable<AccessTokenEnvelope> {
        return service.signIn(body)
                .lift<AccessTokenEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
    }

    override fun oAuth(body: OauthBody): Flowable<AccessTokenEnvelope> {
        return service.oAuth(body)
                .lift<AccessTokenEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
    }

    override fun updateProfile(userId: String, body: User): Flowable<User> {
        return service.updateProfile(userId, body)
                .lift<ProfileEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data.user }
    }

    override fun userByUsername(username: String): Flowable<User> {
        return service.getUserByUsername(username)
                .lift<ProfileEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data.user }
    }

    override fun linkAccounts(userId: String, body: OauthBody): Flowable<List<Account>> {

        return service.link(userId, body)
                .lift<AccountsEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data.accounts }
    }

    override fun postNote(note: Note): Flowable<Note> {
        return service.postNote(note)
                .lift<PostNoteEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data }
    }

    override fun notes(): Flowable<List<Note>> {
        return service.notes()
                .lift<NotesEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data.list }
    }

    override fun getNote(nid: String): Flowable<Note> {
        return service.getNote(nid)
                .lift<NoteEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data }
    }

    override fun updateNote(note: Note): Flowable<Note> {
        return service.updateNote(note.nid, note)
                .lift<NoteEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data }
    }

    override fun deleteNote(nid: String): Flowable<Note> {
        return service.deleteNote(nid)
                .lift<NoteEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data }
    }

    override fun searchLocation(query: String): Flowable<List<Prediction>> {
        return service.locations(query)
                .lift<PredictionsEnvelope>(apiErrorOperator())
                .subscribeOn(Schedulers.io())
                .map { it.data.list }
    }

    /**
     * Utility to create a new [ApiErrorOperator], saves us from littering references to gson throughout the client.
     */
    private fun <T : Envelope> apiErrorOperator(): ApiErrorOperator<T> {
        return Operators.apiError(Gson())
    }
}
