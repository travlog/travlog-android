package com.travlog.android.apps.viewmodels

import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.bus.NoteEvent
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.activities.PostNoteActivity
import com.travlog.android.apps.viewmodels.errors.PostNoteViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.PostNoteViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.PostNoteViewModelOutputs

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject

import com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen

class PostNoteViewModel(environment: Environment) : ActivityViewModel<PostNoteActivity>(environment), PostNoteViewModelInputs, PostNoteViewModelOutputs, PostNoteViewModelErrors {

    private val apiClient: ApiClientType = environment.apiClient

    private val title = PublishSubject.create<String>()
    private val saveClick = PublishSubject.create<Optional<*>>()

    private val back = CompletableSubject.create()

    val inputs: PostNoteViewModelInputs = this
    val outputs: PostNoteViewModelOutputs = this
    val errors: PostNoteViewModelErrors = this

    init {

        title
                .map { title ->
                    val note = Note()
                    note.title = title
                    note
                }
                .compose<Note>(takeWhen(saveClick))
                .switchMap({ note ->
                    this.post(note)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                })
                .compose(bindToLifecycle<Note>())
                .subscribe(this::postSuccess)
    }

    private fun postSuccess(note: Note) {
        NoteEvent.getInstance().post(note)
        back.onComplete()
    }

    private fun post(note: Note): Observable<Note> {
        return apiClient.postNote(note)
                .compose(neverApiError())
                .compose(neverError())
                .toObservable()
    }

    override fun title(title: String) {
        this.title.onNext(title)
    }

    override fun saveClick() {
        this.saveClick.onNext(Optional<Any>(null))
    }

    override fun back(): Completable {
        return back
    }
}
