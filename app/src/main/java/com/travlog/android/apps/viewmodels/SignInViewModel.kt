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
import android.app.Activity.RESULT_OK
import android.text.TextUtils
import android.util.Pair
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.internal.CallbackManagerImpl
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.CurrentUserType
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.transformers.Transformers.combineLatestPair
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.libs.rx.transformers.Transformers.pipeApiErrorsTo
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.services.apirequests.OauthBody
import com.travlog.android.apps.services.apirequests.SignInBody
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope
import com.travlog.android.apps.services.apiresponses.Envelope
import com.travlog.android.apps.ui.activities.SignInActivity
import com.travlog.android.apps.viewmodels.errors.SignInViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.SignInViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.SignInViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class SignInViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<SignInActivity>(environment),
        SignInViewModelInputs, SignInViewModelOutputs, SignInViewModelErrors {

    private val client: ApiClientType = environment.apiClient
    private val currentUser: CurrentUserType = environment.currentUser
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    private val firebaseAccessToken = PublishSubject.create<String>()
    private val loginId = PublishSubject.create<String>()
    private val password = PublishSubject.create<String>()
    private val signInClick = PublishSubject.create<Optional<*>>()
    private val signInError = PublishSubject.create<Envelope>()

    private val startMainActivity = CompletableSubject.create()
    private val facebookAuthorizationError = BehaviorSubject.create<FacebookException>()
    private val setSignInButtonEnabled = BehaviorSubject.create<Boolean>()

    val inputs: SignInViewModelInputs = this
    val outputs: SignInViewModelOutputs = this
    val errors: SignInViewModelErrors = this

    init {
        activityResult()
                .filter { it.requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode() }
                .compose(bindToLifecycle())
                .subscribe { callbackManager.onActivityResult(it.requestCode, it.resultCode, it.intent) }

        activityResult()
                .filter { it.requestCode == SIGN_IN_WITH_GOOGLE }
                .filter { it.resultCode == RESULT_OK }
                .map { it.intent }
                .map(GoogleSignIn::getSignedInAccountFromIntent)
                .map { it.result.idToken }
                .switchMap {
                    this.oAuth(it, "google")
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                }
                .compose(bindToLifecycle())
                .subscribe(this::signInSuccess)

        firebaseAccessToken
                .map { accessToken -> Pair.create(accessToken, "facebook") }
                .switchMap { ap ->
                    this.oAuth(ap.first, ap.second)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                }
                .compose(bindToLifecycle())
                .subscribe(this::signInSuccess)

        facebookAuthorizationError
                .compose(bindToLifecycle())
                .subscribe(this::clearFacebookSession)

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = loginResult.accessToken

                this@SignInViewModel.firebaseAccessToken.onNext(accessToken.token)
            }

            override fun onCancel() {
                Timber.d("facebook onCancel: ")
            }

            override fun onError(error: FacebookException) {
                Timber.w(error.cause, "facebook onError: %s", error.toString())
                facebookAuthorizationError.onNext(error)
            }
        })

        duplicatedError()
                .compose(bindToLifecycle())
                .subscribe { msg -> Timber.d("SignInViewModel: %s", msg) }

        val loginIdAndPassword: Observable<Pair<String, String>> = loginId.compose(combineLatestPair(password))

        val isValid: Observable<Boolean> = loginIdAndPassword
                .map { this.isValid(it.first, it.second) }

        isValid
                .compose(bindToLifecycle())
                .subscribe(setSignInButtonEnabled)

        loginIdAndPassword
                .compose<Pair<String, String>>(takeWhen(signInClick))
                .switchMap({
                    this.signIn(it.first, it.second)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                })
                .compose(bindToLifecycle())
                .subscribe(this::signInSuccess)
    }

    private fun signInSuccess(accessTokenEnvelope: AccessTokenEnvelope) {
        currentUser.login(accessTokenEnvelope.data.user, accessTokenEnvelope.data.accessToken)
        startMainActivity.onComplete()
    }

    private fun isValid(loginId: String, password: String): Boolean {
        return !TextUtils.isEmpty(loginId) && !TextUtils.isEmpty(password)
    }

    private fun clearFacebookSession(e: FacebookException) {
        LoginManager.getInstance().logOut()
    }

    private fun oAuth(accessToken: String, provider: String): Observable<AccessTokenEnvelope> {
        val body = OauthBody()
        body.token = accessToken
        body.provider = provider

        return client.oAuth(body)
                .compose(pipeApiErrorsTo(signInError))
                .compose(neverError())
                .toObservable()
    }

    private fun signIn(loginId: String, password: String): Observable<AccessTokenEnvelope> {
        val body = SignInBody(loginId, password)

        return client.signIn(body)
                .compose(pipeApiErrorsTo(signInError))
                .compose(neverError())
                .toObservable()
    }

    override fun loginId(loginId: String) {
        this.loginId.onNext(loginId)
    }

    override fun signInClick() {
        signInClick.onNext(Optional<Any>(null))
    }

    override fun password(password: String) {
        this.password.onNext(password)
    }

    override fun startMainActivity(): Completable {
        return startMainActivity
    }

    override fun showFacebookAuthorizationErrorDialog(): Observable<String> {
        return this.facebookAuthorizationError
                .map { it.localizedMessage }
    }

    override fun setSignInButtonEnabled(): Observable<Boolean> {
        return setSignInButtonEnabled
    }

    override fun duplicatedError(): Observable<String> {
        return signInError.filter { it.isDuplicatedUser }
                .map { errorEnvelope -> errorEnvelope.err.msg }
    }
}
