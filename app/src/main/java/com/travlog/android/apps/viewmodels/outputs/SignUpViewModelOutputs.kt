package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Completable
import io.reactivex.Observable

interface SignUpViewModelOutputs {

    fun setNextButtonEnabled(): Observable<Boolean>

    fun signUpSuccess(): Completable
}
