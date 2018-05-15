package com.travlog.android.apps.viewmodels.outputs

import com.travlog.android.apps.models.Note

import io.reactivex.Observable

interface MainViewModelOutputs {

    fun updateData(): Observable<List<Note>>

    fun showNoteDetailsActivity(): Observable<Note>

    fun updateNote(): Observable<Note>

    fun deleteNote(): Observable<Int>
}
