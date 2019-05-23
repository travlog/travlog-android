package com.travlog.android.apps.viewmodels

import android.annotation.SuppressLint
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.db.realm.RealmHelper
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.bus.NoteEvent
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.IntentKey.NOTE_ID
import com.travlog.android.apps.ui.activities.EditNoteActivity
import com.travlog.android.apps.viewmodels.errors.EditNoteViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.EditNoteViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.EditNoteViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@SuppressLint("CheckResult")
class EditNoteViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<EditNoteActivity>(environment),
        EditNoteViewModelInputs, EditNoteViewModelOutputs, EditNoteViewModelErrors {

    private val apiClient: ApiClientType = environment.apiClient

    private var note: Note? = null

    private val title = PublishSubject.create<String>()
    private val saveClick = PublishSubject.create<Optional<*>>()

    private val setTitleText = BehaviorSubject.create<String>()
    private val back = CompletableSubject.create()

    val inputs: EditNoteViewModelInputs = this
    val outputs: EditNoteViewModelOutputs = this
    val errors: EditNoteViewModelErrors = this

    init {
        intent()
                .compose(bindToLifecycle())
                .map { i -> i.getStringExtra(NOTE_ID) ?: "" }
                .map { RealmHelper.getNoteAsync(realm, it) }
                .doOnNext { this.note = it }
                .map { it.title }
                .subscribe { setTitleText.onNext(it) }

        title
                .compose(bindToLifecycle())
                .subscribe { note!!.title = it }

        saveClick
                .switchMap {
                    this.update(this.note!!)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                }
                .subscribe(this::updateSuccess)
    }

    private fun updateSuccess(note: Note) {
        NoteEvent.getInstance().post(note)
        back.onComplete()
    }

    private fun update(note: Note): Observable<Note> {
        return apiClient.updateNote(note)
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

    override fun setTitleText(): Observable<String> {
        return setTitleText
    }

    override fun back(): Completable {
        return back
    }
}
