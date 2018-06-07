package com.travlog.android.apps.viewmodels.outputs

import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Note

import io.reactivex.Completable
import io.reactivex.Observable

interface NoteViewModelOutputs {

    fun setTitleText(): Observable<String>

    fun updateDestinations(): Observable<List<Destination>>

    fun showEditNoteActivity(): Observable<Note>

    fun finish(): Completable
}
