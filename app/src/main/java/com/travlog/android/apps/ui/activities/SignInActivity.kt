package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE
import com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_UP_FLOW
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.viewmodels.SignInViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_sign_in.*
import timber.log.Timber

class SignInActivity : BaseActivity<SignInViewModel>() {

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_sign_in)

        this.sign_in_with_facebook_button.setReadPermissions("email", "public_profile")

        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(Scopes.EMAIL))
                .requestIdToken(getString(R.string.default_web_client_id))
                .build().let {
//                    googleApiClient = GoogleApiClient.Builder(this)
//                            .enableAutoManage(this) { Timber.d("onConnectionFailed: %s", it.errorMessage) }
//                            .addApi(Auth.GOOGLE_SIGN_IN_API, it)
//                            .build()
                }

        close_button.clicks()
                .compose(bindToLifecycle())
                .subscribe { this.back() }

        sign_in_with_google_button.clicks()
                .compose(bindToLifecycle())
                .subscribe { this.startSignInWithGoogleActivity() }

        viewModel?.apply {
            login_id.textChanges()
                    .map { it.toString() }
                    .compose(bindToLifecycle())
                    .subscribe(inputs::loginId)

            password.textChanges()
                    .map { it.toString() }
                    .compose(bindToLifecycle())
                    .subscribe(inputs::password)

            sign_in_button.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { inputs.signInClick() }

            sign_up_button.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { startSignUp() }

            addDisposable(
                    outputs.startMainActivity()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { this@SignInActivity.startMainActivity() }
            )

            setSignInButtonEnabled()
                    .compose(bindToLifecycle())
                    .subscribe { setSignInButtonEnabled(it) }
        }
    }

    private fun startMainActivity() =
            Intent(this, MainActivity::class.java).let {
                startActivity(it)
                back()
            }

    private fun startSignInWithGoogleActivity() =
            Auth.GoogleSignInApi.getSignInIntent(googleApiClient).let {
                startActivityForResult(it, SIGN_IN_WITH_GOOGLE)
            }

    private fun startSignUp() =
            Intent(this, SignUpActivity::class.java).let {
                startActivityForResult(it, SIGN_UP_FLOW)
            }

    private fun setSignInButtonEnabled(enabled: Boolean) {
        this.sign_in_button.isEnabled = enabled
    }

    override fun exitTransition(): Pair<Int, Int>? =
            Pair.create(android.R.anim.fade_in, R.anim.slide_out_bottom)
}
