package com.travlog.android.apps.viewmodels.outputs

import com.travlog.android.apps.models.Destination
import io.reactivex.Completable
import io.reactivex.Observable

interface PostNoteViewModelOutputs {

    fun setSaveButtonEnabled(): Observable<Boolean>

    fun addDestination(): Observable<Destination>

    fun setResultAndBack(): Completable
}
