package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.util.Pair
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.jakewharton.rxbinding2.view.RxView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.LinkedAccountsViewModel
import kotlinx.android.synthetic.main.a_linked_accounts.*
import timber.log.Timber
import java.util.*

@RequiresActivityViewModel(LinkedAccountsViewModel::class)
class LinkedAccountsActivity : BaseActivity<LinkedAccountsViewModel>() {

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_linked_accounts)
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this
                ) { connectionResult -> Timber.d("onConnectionFailed: %s", connectionResult.errorMessage) }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        RxView.clicks(this.facebook)
                .compose(bindToLifecycle())
                .subscribe { LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile")) }

        RxView.clicks(this.google)
                .compose(bindToLifecycle())
                .subscribe { this.startSignInWithGoogleActivity() }
    }

    private fun startSignInWithGoogleActivity() {
        val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(intent, SIGN_IN_WITH_GOOGLE)
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
