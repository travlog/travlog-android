package com.travlog.android.apps.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.widget.Button
import android.widget.EditText

import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.viewmodels.SignInViewModel

import butterknife.BindView
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

import com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE
import com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_UP_FLOW
import kotlinx.android.synthetic.main.a_sign_in.*

@RequiresActivityViewModel(SignInViewModel::class)
class SignInActivity : BaseActivity<SignInViewModel>() {

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_sign_in)

        this.sign_in_with_facebook_button.setReadPermissions("email", "public_profile")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(Scopes.EMAIL))
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this
                ) { connectionResult -> Timber.d("onConnectionFailed: %s", connectionResult.errorMessage) }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        RxView.clicks(this.close_button)
                .compose(bindToLifecycle())
                .subscribe { this.back() }

        RxView.clicks(this.sign_in_with_google_button)
                .compose(bindToLifecycle())
                .subscribe { this.startSignInWithGoogleActivity() }

        RxTextView.textChanges(this.login_id)
                .map { it.toString() }
                .compose(bindToLifecycle())
                .subscribe(viewModel!!::loginId)

        RxTextView.textChanges(this.password)
                .map { it.toString() }
                .compose(bindToLifecycle())
                .subscribe(viewModel!!::password)

        RxView.clicks(this.sign_in_button)
                .compose(bindToLifecycle())
                .subscribe { this.viewModel!!.signInClick() }

        RxView.clicks(this.sign_up_button)
                .compose(bindToLifecycle())
                .subscribe { this.startSignUp() }

        viewModel!!.outputs.signInSuccess()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe { this.back() }

        viewModel!!.outputs.setSignInButtonEnabled()
                .compose(bindToLifecycle())
                .subscribe(this::setSignInButtonEnabled)
    }

    private fun startSignInWithGoogleActivity() {
        val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(intent, SIGN_IN_WITH_GOOGLE)
    }

    private fun startSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivityForResult(intent, SIGN_UP_FLOW)
    }

    private fun setSignInButtonEnabled(enabled: Boolean) {
        this.sign_in_button.isEnabled = enabled
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return Pair.create(android.R.anim.fade_in, R.anim.slide_out_bottom)
    }
}
