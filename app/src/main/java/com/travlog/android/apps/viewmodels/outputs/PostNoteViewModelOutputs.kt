package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Completable
import io.reactivex.Observable

interface PostNoteViewModelOutputs {

    fun setSaveButtonEnabled(): Observable<Boolean>

    fun setResultAndBack(): Completable
}
