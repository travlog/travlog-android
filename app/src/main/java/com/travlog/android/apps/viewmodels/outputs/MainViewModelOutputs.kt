package com.travlog.android.apps.viewmodels.outputs

import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.models.Note

import io.reactivex.Observable

interface MainViewModelOutputs {

    fun updateData(): Observable<List<Note>>

    fun showNoteDetailsActivity(): Observable<Note>

    fun clearNotes(): Observable<Optional<*>>

    fun updateNotes(): Observable<Note>

    fun deleteNote(): Observable<String>
}
