package com.travlog.android.apps.viewmodels.outputs

import com.travlog.android.apps.models.Note

import io.reactivex.Completable
import io.reactivex.Observable

interface NoteViewModelOutputs {

    fun setTitleText(): Observable<String>

    fun showEditNoteActivity(): Observable<Note>

    fun back(): Completable
}
