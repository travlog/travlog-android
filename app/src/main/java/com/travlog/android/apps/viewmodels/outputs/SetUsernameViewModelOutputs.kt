package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Completable
import io.reactivex.Observable

interface SetUsernameViewModelOutputs {

    fun setDoneButtonEnabled(): Observable<Boolean>

    fun back(): Completable
}
