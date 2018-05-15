package com.travlog.android.apps.viewmodels.errors

import io.reactivex.Observable

interface SignUpViewModelErrors {

    fun duplicatedError(): Observable<String>
}
