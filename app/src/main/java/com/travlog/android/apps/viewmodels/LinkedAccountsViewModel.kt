package com.travlog.android.apps.viewmodels

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.internal.CallbackManagerImpl
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.models.Account
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.services.apirequests.OauthBody
import com.travlog.android.apps.ui.activities.LinkedAccountsActivity
import com.travlog.android.apps.viewmodels.errors.LinkedAccountsViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.LinkedAccountsViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.LinkedAccountsViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class LinkedAccountsViewModel(environment: Environment) : ActivityViewModel<LinkedAccountsActivity>(environment), LinkedAccountsViewModelInputs, LinkedAccountsViewModelOutputs, LinkedAccountsViewModelErrors {

    private val apiClient: ApiClientType = environment.apiClient
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    private val facebookAccessToken = PublishSubject.create<String>()

    private val signInSuccess = BehaviorSubject.create<Optional<*>>()
    private val facebookAuthorizationError = BehaviorSubject.create<FacebookException>()

    val inputs: LinkedAccountsViewModelInputs = this
    val outputs: LinkedAccountsViewModelOutputs = this
    val errors: LinkedAccountsViewModelErrors = this

    init {
        activityResult()
                .filter { it.requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode() }
                .compose(bindToLifecycle())
                .subscribe { callbackManager.onActivityResult(it.requestCode, it.resultCode, it.intent) }

        activityResult()
                .filter { it.requestCode == SIGN_IN_WITH_GOOGLE }
                .map({ it.intent })
                .map<Task<GoogleSignInAccount>>(GoogleSignIn::getSignedInAccountFromIntent)
                .map({ it.result })
                .map { it.idToken }
                .switchMap { this.link(environment.currentUser.getUser()!!.userId, it, "google") }
                .compose(bindToLifecycle())
                .subscribe()

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = loginResult.accessToken
                facebookAccessToken.onNext(accessToken.token)
            }

            override fun onCancel() {
                Timber.d("facebook onCancel: ")
            }

            override fun onError(error: FacebookException) {
                Timber.w(error.cause, "facebook onError: %s", error.toString())
                facebookAuthorizationError.onNext(error)
            }
        })

        facebookAccessToken
                .switchMap { this.link(environment.currentUser.getUser()!!.userId, it, "facebook") }
                .compose(bindToLifecycle())
                .subscribe()
    }

    private fun link(userId: String, token: String, provider: String): Observable<List<Account>> {
        val body = OauthBody()
        body.token = token
        body.provider = provider

        return apiClient.linkAccounts(userId, body)
                .compose(neverError())
                .toObservable()
    }
}
