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
import android.text.TextUtils
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.CurrentUserType
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.libs.rx.transformers.Transformers.pipeApiErrorsTo
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.models.User
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.services.apiresponses.Envelope
import com.travlog.android.apps.ui.activities.SetUsernameActivity
import com.travlog.android.apps.viewmodels.errors.SetUsernameViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.SetUsernameViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.SetUsernameViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("CheckResult")
class SetUsernameViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<SetUsernameActivity>(environment),
        SetUsernameViewModelInputs, SetUsernameViewModelOutputs, SetUsernameViewModelErrors {

    private val currentUser: CurrentUserType = environment.currentUser
    private val apiClient: ApiClientType = environment.apiClient

    val inputs: SetUsernameViewModelInputs = this
    val outputs: SetUsernameViewModelOutputs = this
    val errors: SetUsernameViewModelErrors = this

    private val username = PublishSubject.create<String>()
    private val doneClick = PublishSubject.create<Optional<*>>()
    private val profileError = PublishSubject.create<Envelope>()

    private val setDoneButtonEnabled = BehaviorSubject.create<Boolean>()
    private val back = CompletableSubject.create()

    init {
        val isValid = username.map<Boolean>(this::isValid)

        isValid
                .compose(bindToLifecycle())
                .subscribe(setDoneButtonEnabled)

        username
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap { username ->
                    this.userByUsername(username)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                }
                .compose(bindToLifecycle())
                .subscribe()

        username
                .compose<String>(takeWhen(doneClick))
                .switchMap({ username ->
                    this.setUsername(currentUser.getUser()!!.userId, username)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                })
                .compose(bindToLifecycle<User>())
                .subscribe(this::setUsernameSuccess)
    }

    private fun setUsernameSuccess(user: User) {
        currentUser.refresh(user)
        back.onComplete()
    }

    private fun isValid(username: String): Boolean {
        return !TextUtils.isEmpty(username)
    }

    private fun userByUsername(username: String): Observable<User> {
        return apiClient.userByUsername(username)
                .compose(pipeApiErrorsTo(profileError))
                .compose(neverError())
                .toObservable()
    }

    private fun setUsername(userId: String, username: String): Observable<User> {
        val user = User()
        user.username = username

        return apiClient.updateProfile(userId, user)
                .compose(pipeApiErrorsTo(profileError))
                .compose(neverError())
                .toObservable()
    }

    override fun username(username: String) {
        this.username.onNext(username)
    }

    override fun doneClick() {
        this.doneClick.onNext(Optional<Any>(null))
    }

    override fun setDoneButtonEnabled(): Observable<Boolean> {
        return setDoneButtonEnabled
    }

    override fun back(): Completable {
        return back
    }
}
