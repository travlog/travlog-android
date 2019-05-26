package com.travlog.android.apps.viewmodels

import android.annotation.SuppressLint
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.db.realm.RealmHelper
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.bus.DeleteNoteEvent
import com.travlog.android.apps.libs.rx.bus.NoteEvent
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.IntentKey.NOTE_ID
import com.travlog.android.apps.ui.activities.NoteDetailsActivity
import com.travlog.android.apps.ui.adapters.DestinationAdapter
import com.travlog.android.apps.ui.viewholders.DestinationViewHolder
import com.travlog.android.apps.viewmodels.errors.NoteViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.NoteViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.NoteViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class NoteDetailsViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<NoteDetailsActivity>(environment),
        NoteViewModelInputs, NoteViewModelOutputs, NoteViewModelErrors, DestinationAdapter.Delegate {

    private val apiClient: ApiClientType = environment.apiClient

    private val note = PublishSubject.create<Note>()
    private val editClick = PublishSubject.create<Optional<*>>()
    private val deleteClick = PublishSubject.create<Optional<*>>()

    private val setTitleText = BehaviorSubject.create<String>()
    private val updateDestinations = BehaviorSubject.create<List<Destination>>()
    private val showEditNoteActivity = BehaviorSubject.create<Note>()
    private val finish = CompletableSubject.create()

    val inputs: NoteViewModelInputs = this
    val outputs: NoteViewModelOutputs = this
    val errors: NoteViewModelErrors = this

    init {
        val noteIntent = intent()
                .map { it.getStringExtra(NOTE_ID) ?: "" }
                .map { RealmHelper.getNoteAsync(realm, it) }

        note
                .compose(bindToLifecycle())
                .doOnNext { Timber.d("note? %s", it) }
                .subscribe { setNote(it) }

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
                        .doOnNext { NoteEvent.getInstance().post(it) },
                NoteEvent.getInstance().observable
        )
                .compose(bindToLifecycle())
                .subscribe(note)
    }

    private fun setNote(note: Note) {
        setTitleText.onNext(note.title)
        updateDestinations.onNext(note.destinations)
    }

    private fun deleteSuccess(noteId: String) {
        // TODO: 2018. 5. 12. note not found error 발생 시에도 delete 성공과 똑같이 처리한다
        DeleteNoteEvent.getInstance().post(noteId)
        finish.onComplete()
    }

    private fun note(noteId: String): Observable<Note> =
            apiClient.getNote(noteId)
                    .compose(neverApiError())
                    .compose(neverError())
                    .toObservable()

    private fun delete(noteId: String): Observable<String> =
            apiClient.deleteNote(noteId)
                    .compose(neverApiError())
                    .compose(neverError())
                    .toObservable()

    override fun setTitleText(): Observable<String> = setTitleText

    override fun updateDestinations(): Observable<List<Destination>> = updateDestinations

    override fun showEditNoteActivity(): Observable<Note> = showEditNoteActivity

    override fun finish(): Completable = finish

    override fun editClick() = this.editClick.onNext(Optional<Any>(null))

    override fun deleteClick() = this.deleteClick.onNext(Optional<Any>(null))

    override fun destinationViewHolderItemClick(viewHolder: DestinationViewHolder, destination: Destination) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
