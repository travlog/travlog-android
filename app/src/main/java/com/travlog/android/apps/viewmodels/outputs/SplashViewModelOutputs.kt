package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Completable

interface SplashViewModelOutputs {

    fun startSignInActivity(): Completable

    fun startMainActivity(): Completable
}
