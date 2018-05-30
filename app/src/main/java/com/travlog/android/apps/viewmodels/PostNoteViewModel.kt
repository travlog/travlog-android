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
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.IntentKey
import com.travlog.android.apps.ui.activities.PostNoteActivity
import com.travlog.android.apps.viewmodels.errors.PostNoteViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.PostNoteViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.PostNoteViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject

class PostNoteViewModel(environment: Environment) : ActivityViewModel<PostNoteActivity>(environment),
        PostNoteViewModelInputs, PostNoteViewModelOutputs, PostNoteViewModelErrors {

    private val apiClient: ApiClientType = environment.apiClient

    private val title = PublishSubject.create<String>()
    private val saveClick = PublishSubject.create<Optional<*>>()

    private val setSaveButtonEnabled = BehaviorSubject.create<Boolean>()
    private val setResultAndBack = CompletableSubject.create()

    val inputs: PostNoteViewModelInputs = this
    val outputs: PostNoteViewModelOutputs = this
    val errors: PostNoteViewModelErrors = this

    init {
        val note = Note()

        val destinationObservable = activityResult()
                .filter { it.requestCode == DESTINATION }
                .filter { it.resultCode == RESULT_OK }
                .map { it.intent?.getParcelableExtra(IntentKey.DESTINATION) as Destination }

        title.map(this::isValid)
                .compose(bindToLifecycle())
                .subscribe(setSaveButtonEnabled)

        Observable.merge(
                title.map {
                    note.title = it
                    note
                },
                destinationObservable.map {
                    note.destinations.add(it)
                    note
                })
                .compose<Note>(takeWhen(saveClick))
                .switchMap {
                    this.post(it)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                }
                .compose(bindToLifecycle())
                .subscribe { postSuccess(it) }
    }

    private fun isValid(title: String): Boolean {
        return title.isNotBlank()
    }

    private fun postSuccess(note: Note) {
        NoteEvent.getInstance().post(note)
        setResultAndBack.onComplete()
    }

    private fun post(note: Note): Observable<Note> =
            apiClient.postNote(note)
                    .compose(neverApiError())
                    .compose(neverError())
                    .toObservable()

    override fun title(title: String) = this.title.onNext(title)

    override fun saveClick() = this.saveClick.onNext(Optional<Any>(null))

    override fun setSaveButtonEnabled(): Observable<Boolean> = setSaveButtonEnabled

    override fun setResultAndBack(): Completable = setResultAndBack
}
