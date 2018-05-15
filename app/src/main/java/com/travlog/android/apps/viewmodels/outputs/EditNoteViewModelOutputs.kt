package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Completable
import io.reactivex.Observable


interface EditNoteViewModelOutputs {

    fun setTitleText(): Observable<String>

    fun back(): Completable
}
