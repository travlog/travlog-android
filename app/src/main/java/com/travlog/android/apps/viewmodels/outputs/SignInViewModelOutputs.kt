package com.travlog.android.apps.viewmodels.outputs

import com.travlog.android.apps.libs.rx.Optional

import io.reactivex.Observable

interface SignInViewModelOutputs {

    /**
     * Emits a boolean to determine whether or not the login button should be enabled.
     */

    fun signInSuccess(): Observable<Optional<*>>

    fun showFacebookAuthorizationErrorDialog(): Observable<String>

    fun setSignInButtonEnabled(): Observable<Boolean>
}
