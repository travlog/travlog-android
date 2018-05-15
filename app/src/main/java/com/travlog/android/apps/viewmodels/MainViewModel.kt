package com.travlog.android.apps.viewmodels

import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.bus.DeleteNoteEvent
import com.travlog.android.apps.libs.rx.bus.NoteEvent
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.activities.MainActivity
import com.travlog.android.apps.ui.adapters.NoteAdapter
import com.travlog.android.apps.ui.viewholders.NoteViewHolder
import com.travlog.android.apps.viewmodels.errors.MainViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.MainViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.MainViewModelOutputs

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

import com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError

class MainViewModel(environment: Environment) : ActivityViewModel<MainActivity>(environment), MainViewModelInputs, MainViewModelOutputs, MainViewModelErrors, NoteAdapter.Delegate {

    private val apiClient: ApiClientType = environment.apiClient

    private val noteItemClick = PublishSubject.create<Note>()

    private val updateData = BehaviorSubject.create<List<Note>>()
    private val showNoteDetailsActivity: Observable<Note>
    private val updateNote: Observable<Note>
    private val deleteNote: Observable<Int>

    val inputs: MainViewModelInputs = this
    val outputs: MainViewModelOutputs = this
    val errors: MainViewModelErrors = this

    init {
        this.showNoteDetailsActivity = this.noteItemClick

        notes()
                .compose(bindToLifecycle())
                .subscribe(updateData::onNext)

        updateNote = NoteEvent.getInstance().observable

        deleteNote = DeleteNoteEvent.getInstance().observable
                .map { note -> note.id }
    }

    private fun notes(): Observable<List<Note>> {
        return apiClient.notes()
                .compose(neverApiError())
                .compose(neverError())
                .toObservable()
    }

    override fun updateData(): Observable<List<Note>> {
        return updateData
    }

    override fun showNoteDetailsActivity(): Observable<Note> {
        return showNoteDetailsActivity
    }

    override fun updateNote(): Observable<Note> {
        return updateNote
    }

    override fun deleteNote(): Observable<Int> {
        return deleteNote
    }

    override fun noteViewHolderItemClick(viewHolder: NoteViewHolder,
                                         note: Note) {

        noteItemClick.onNext(note)
    }
}
