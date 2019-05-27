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
                .map { RealmHelper.getNote(realm, it) }
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
