package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Completable

import io.reactivex.Observable

interface SignInViewModelOutputs {

    /**
     * Emits a boolean to determine whether or not the login button should be enabled.
     */

    fun showFacebookAuthorizationErrorDialog(): Observable<String>

    fun setSignInButtonEnabled(): Observable<Boolean>

    fun startMainActivity(): Completable
}
