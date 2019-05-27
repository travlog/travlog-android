/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
