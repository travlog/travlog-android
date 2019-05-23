package com.travlog.android.apps.viewmodels

import android.util.Pair
import android.util.Patterns
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.transformers.Transformers.combineLatestPair
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.libs.rx.transformers.Transformers.pipeApiErrorsTo
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.services.apirequests.SignUpBody
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope
import com.travlog.android.apps.services.apiresponses.Envelope
import com.travlog.android.apps.ui.activities.SignUpActivity
import com.travlog.android.apps.viewmodels.errors.SignUpViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.SignUpViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.SignUpViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<SignUpActivity>(environment), SignUpViewModelInputs, SignUpViewModelOutputs, SignUpViewModelErrors {

    private val apiClient: ApiClientType = environment.apiClient

    private val email = PublishSubject.create<String>()
    private val password = PublishSubject.create<String>()
    private val signUpClick = PublishSubject.create<Optional<*>>()
    private val signUpError = PublishSubject.create<Envelope>()

    private val setNextButtonEnabled = BehaviorSubject.create<Boolean>()
    private val signUpSuccess = CompletableSubject.create()

    val inputs: SignUpViewModelInputs = this
    val outputs: SignUpViewModelOutputs = this
    val errors: SignUpViewModelErrors = this

    init {
        val isValidEmail = this.email.map { this.isValidEmail(it) }

        val isValidPassword = this.password.map { this.isValidPassword(it) }

        Observable.merge(
                isValidEmail,
                isValidPassword
        )
                .compose(bindToLifecycle())
                .subscribe { setNextButtonEnabled.onNext(it) }


        val emailAndPassword: Observable<Pair<String, String>> = email.compose(combineLatestPair(password))

        emailAndPassword
                .compose<Pair<String, String>>(takeWhen(signUpClick))
                .switchMap {
                    this.signUp(it.first, it.second)
                            .doOnSubscribe {

                            }
                            .doAfterTerminate {

                            }
                }
                .compose(bindToLifecycle())
                .subscribe {
                    environment.currentUser.login(it.data.user, it.data.accessToken)
                    signUpSuccess.onComplete()
                }

        duplicatedError()
                .compose(bindToLifecycle())
                .subscribe { msg -> Timber.d("SignUpViewModel: duplicatedError: %s", msg) }
    }

    private fun isValidEmail(email: String): Boolean {
        return Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)
    }

    private fun isValidPassword(password: String): Boolean {
        return password.isNotEmpty()
    }

    private fun signUp(email: String, password: String): Observable<AccessTokenEnvelope> {

        val body = SignUpBody()
        body.email = email
        body.password = password

        return apiClient.signUp(body)
                .compose(pipeApiErrorsTo(signUpError))
                .compose(neverError())
                .toObservable()
    }

    override fun email(email: String) {
        this.email.onNext(email)
    }

    override fun password(password: String) {
        this.password.onNext(password)
    }

    override fun signUpClick() {
        this.signUpClick.onNext(Optional<Any>(null))
    }

    override fun setNextButtonEnabled(): Observable<Boolean> {
        return setNextButtonEnabled
    }

    override fun signUpSuccess(): Completable {
        return signUpSuccess
    }

    override fun duplicatedError(): Observable<String> {
        return signUpError.filter { it.isDuplicatedUser }.map { it.err.msg }
    }
}
