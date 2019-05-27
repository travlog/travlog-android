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

import android.text.TextUtils

import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.ui.activities.MyPageActivity
import com.travlog.android.apps.viewmodels.errors.MyPageViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.MyPageViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.MyPageViewModelOutputs

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MyPageViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<MyPageActivity>(environment),
        MyPageViewModelInputs, MyPageViewModelOutputs, MyPageViewModelErrors {

    val inputs: MyPageViewModelInputs = this
    val outputs: MyPageViewModelOutputs = this
    val errors: MyPageViewModelErrors = this

    private val setProfilePicture = BehaviorSubject.create<String>()
    private val setUsernameText = BehaviorSubject.create<String>()

    init {
        environment.currentUser.loggedInUser()
                .map { user -> if (TextUtils.isEmpty(user.username)) "" else user.username }
                .compose(bindToLifecycle())
                .subscribe(setUsernameText)

        environment.currentUser.loggedInUser()
                .map { user -> if (TextUtils.isEmpty(user.profilePicture)) "" else user.profilePicture }
                .compose(bindToLifecycle())
                .subscribe(setProfilePicture)
    }

    override fun setProfilePicture(): Observable<String> {
        return setProfilePicture
    }

    override fun setUsernameText(): Observable<String> {
        return setUsernameText
    }
}
