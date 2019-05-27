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
import javax.inject.Inject

@SuppressLint("CheckResult")
class MainViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<MainActivity>(environment),
        MainViewModelInputs, MainViewModelOutputs, MainViewModelErrors, NoteAdapter.Delegate {

    private val apiClient: ApiClientType = environment.apiClient

    private val noteItemClick = PublishSubject.create<Note>()
    private val loadMore = PublishSubject.create<Optional<*>>()
    private val refresh = PublishSubject.create<Optional<*>>()

    private val clearNotes = BehaviorSubject.create<Optional<*>>()
    private val updateData = BehaviorSubject.create<List<Note>>()
    private val showNoteDetailsActivity: Observable<Note>
    private val updateNote: Observable<Note>
    private val deleteNote: Observable<String>

    val inputs: MainViewModelInputs = this
    val outputs: MainViewModelOutputs = this
    val errors: MainViewModelErrors = this

    init {
        this.showNoteDetailsActivity = this.noteItemClick

        Observable.merge(loadMore, refresh.doOnNext { clearNotes.onNext(Optional(null)) })
//                .switchMap {
//                    this.notes()
//                            .doOnSubscribe {
//                            }
//                            .doAfterTerminate {
//                            }
//                }
                .map { RealmHelper.getAllNotes(realm) }
                .compose(bindToLifecycle())
                .subscribe { updateData.onNext(it) }

        updateNote = NoteEvent.getInstance().observable

        deleteNote = DeleteNoteEvent.getInstance().observable
    }

    private fun notes(): Observable<List<Note>> =
            apiClient.notes()
                    .compose(neverApiError())
                    .compose(neverError())
                    .toObservable()

    override fun loadMore() = loadMore.onNext(Optional(null))

    override fun refresh() = refresh.onNext(Optional(null))

    override fun clearNotes(): Observable<Optional<*>> = clearNotes

    override fun updateData(): Observable<List<Note>> = updateData

    override fun showNoteDetailsActivity(): Observable<Note> = showNoteDetailsActivity

    override fun updateNotes(): Observable<Note> = updateNote

    override fun deleteNote(): Observable<String> = deleteNote

    override fun noteViewHolderItemClick(viewHolder: NoteViewHolder, note: Note) = noteItemClick.onNext(note)
}
