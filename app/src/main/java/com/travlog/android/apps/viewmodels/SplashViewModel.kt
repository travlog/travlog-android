package com.travlog.android.apps.viewmodels

import android.annotation.SuppressLint
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.ui.activities.SplashActivity
import com.travlog.android.apps.viewmodels.outputs.SplashViewModelOutputs
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import javax.inject.Inject

@SuppressLint("CheckResult")
class SplashViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<SplashActivity>(environment),
        SplashViewModelOutputs {

    private val startSignInActivity = CompletableSubject.create()
    private val startMainActivity = CompletableSubject.create()

    val outputs = this

    init {
        environment.currentUser.loggedOutUser()
                .compose(bindToLifecycle())
                .subscribe { startSignInActivity.onComplete() }

        environment.currentUser.loggedInUser()
                .compose(bindToLifecycle())
                .subscribe { startMainActivity.onComplete() }
    }

    override fun startSignInActivity(): Completable = startSignInActivity

    override fun startMainActivity(): Completable = startMainActivity
}
