package com.travlog.android.apps.viewmodels

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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class SignInViewModel
constructor(environment: Environment) : ActivityViewModel<SignInActivity>(environment), SignInViewModelInputs, SignInViewModelOutputs, SignInViewModelErrors {

    private val client: ApiClientType = environment.apiClient
    private val currentUser: CurrentUserType = environment.currentUser
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    private val firebaseAccessToken = PublishSubject.create<String>()
    private val loginId = PublishSubject.create<String>()
    private val password = PublishSubject.create<String>()
    private val signInClick = PublishSubject.create<Optional<*>>()
    private val signInError = PublishSubject.create<Envelope>()

    private val signInSuccess = BehaviorSubject.create<Optional<*>>()
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
                .map { GoogleSignIn::getSignedInAccountFromIntent as GoogleSignInAccount }
                .map { it.idToken }
                .switchMap { accessToken ->
                    this.oAuth(accessToken, "google")
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
        signInSuccess.onNext(Optional<Any>(null))
    }

    private fun isValid(loginId: String, password: String): Boolean {
        return !TextUtils.isEmpty(loginId) && !TextUtils.isEmpty(password)
    }

    private fun clearFacebookSession(e: FacebookException) {
        LoginManager.getInstance().logOut()
    }

    private fun oAuth(accessToken: String,
                      provider: String): Observable<AccessTokenEnvelope> {

        val body = OauthBody()
        body.token = accessToken
        body.provider = provider

        return client.oAuth(body)
                .compose(neverError())
                .toObservable()
    }

    private fun signIn(loginId: String, password: String): Observable<AccessTokenEnvelope> {
        val body = SignInBody()
        body.loginId = loginId
        body.password = password

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

    override fun signInSuccess(): Observable<Optional<*>> {
        return signInSuccess
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
