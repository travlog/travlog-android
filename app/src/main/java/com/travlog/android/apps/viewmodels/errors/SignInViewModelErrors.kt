package com.travlog.android.apps.viewmodels.errors

import io.reactivex.Observable

interface SignInViewModelErrors {

    fun duplicatedError(): Observable<String>
}
