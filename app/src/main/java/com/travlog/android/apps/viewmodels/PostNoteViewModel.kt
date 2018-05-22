package com.travlog.android.apps.viewmodels

import android.app.Activity.RESULT_OK
import com.travlog.android.apps.libs.ActivityRequestCodes.DESTINATION
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.bus.NoteEvent
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.IntentKey
import com.travlog.android.apps.ui.activities.PostNoteActivity
import com.travlog.android.apps.viewmodels.errors.PostNoteViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.PostNoteViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.PostNoteViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class PostNoteViewModel(environment: Environment) : ActivityViewModel<PostNoteActivity>(environment), PostNoteViewModelInputs, PostNoteViewModelOutputs, PostNoteViewModelErrors {

    private val apiClient: ApiClientType = environment.apiClient

    private val title = PublishSubject.create<String>()
    private val saveClick = PublishSubject.create<Optional<*>>()

    private val back = CompletableSubject.create()

    val inputs: PostNoteViewModelInputs = this
    val outputs: PostNoteViewModelOutputs = this
    val errors: PostNoteViewModelErrors = this

    init {
        activityResult()
                .filter { it.requestCode == DESTINATION }
                .filter { it.resultCode == RESULT_OK }
                .map { it.intent?.getParcelableExtra(IntentKey.DESTINATION) as Destination }
                .compose(bindToLifecycle())
                .subscribe { Timber.d("%s", it) }

        title
                .map {
                    val note = Note()
                    note.title = it
                    note
                }
                .compose<Note>(takeWhen(saveClick))
                .switchMap { note ->
                    this.post(note)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                }
                .compose(bindToLifecycle())
                .subscribe(this::postSuccess)
    }

    private fun postSuccess(note: Note) =
            NoteEvent.getInstance().post(note).run {
                back.onComplete()
            }

    private fun post(note: Note): Observable<Note> =
            apiClient.postNote(note)
                    .compose(neverApiError())
                    .compose(neverError())
                    .toObservable()

    override fun title(title: String) = this.title.onNext(title)

    override fun saveClick() = this.saveClick.onNext(Optional<Any>(null))

    override fun back(): Completable = back
}
