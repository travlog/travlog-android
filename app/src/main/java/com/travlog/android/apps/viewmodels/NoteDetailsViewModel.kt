package com.travlog.android.apps.viewmodels

import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.bus.DeleteNoteEvent
import com.travlog.android.apps.libs.rx.bus.NoteEvent
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.IntentKey.NOTE
import com.travlog.android.apps.ui.activities.NoteDetailsActivity
import com.travlog.android.apps.viewmodels.errors.NoteViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.NoteViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.NoteViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject

class NoteDetailsViewModel
constructor(environment: Environment) : ActivityViewModel<NoteDetailsActivity>(environment),
        NoteViewModelInputs, NoteViewModelOutputs, NoteViewModelErrors {

    private val apiClient: ApiClientType = environment.apiClient

    private val note = PublishSubject.create<Note>()
    private val editClick = PublishSubject.create<Optional<*>>()
    private val deleteClick = PublishSubject.create<Optional<*>>()

    private val setTitleText = BehaviorSubject.create<String>()
    private val showEditNoteActivity = BehaviorSubject.create<Note>()
    private val back = CompletableSubject.create()

    val inputs: NoteViewModelInputs = this
    val outputs: NoteViewModelOutputs = this
    val errors: NoteViewModelErrors = this

    init {

        val noteIntent = intent()
                .map { i -> i.getParcelableExtra(NOTE) as Note }

        note
                .compose(bindToLifecycle())
                .subscribe { note -> setTitleText.onNext(note.title) }

        note
                .compose<Note>(takeWhen(editClick))
                .compose(bindToLifecycle())
                .subscribe(showEditNoteActivity)

        note
                .compose<Note>(takeWhen(deleteClick))
                .switchMap {
                    this.delete(it.id)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                }
                .compose(bindToLifecycle())
                .subscribe(this::deleteSuccess)

        Observable.merge(
                noteIntent,
                noteIntent.switchMap { note -> this.note(note.id) }
                        .doOnNext({ NoteEvent.getInstance().post(it) }),
                NoteEvent.getInstance().observable
        )
                .compose(bindToLifecycle())
                .subscribe(note)
    }

    private fun deleteSuccess(note: Note) {
        // TODO: 2018. 5. 12. note not found error 발생 시에도 delete 성공과 똑같이 처리한다
        DeleteNoteEvent.getInstance().post(note)
        back.onComplete()
    }

    private fun note(noteId: Int): Observable<Note> {
        return apiClient.getNote(noteId)
                .compose(neverApiError())
                .compose(neverError())
                .toObservable()
    }

    private fun delete(noteId: Int): Observable<Note> {
        return apiClient.deleteNote(noteId)
                .compose(neverApiError())
                .compose(neverError())
                .toObservable()
    }

    override fun setTitleText(): Observable<String> {
        return setTitleText
    }

    override fun showEditNoteActivity(): Observable<Note> {
        return showEditNoteActivity
    }

    override fun back(): Completable {
        return back
    }

    override fun editClick() {
        this.editClick.onNext(Optional<Any>(null))
    }

    override fun deleteClick() {
        this.deleteClick.onNext(Optional<Any>(null))
    }
}
