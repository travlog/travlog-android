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

package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.util.Pair
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.jakewharton.rxbinding3.view.clicks
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.LinkedAccountsViewModel
import kotlinx.android.synthetic.main.a_linked_accounts.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LinkedAccountsActivity : BaseActivity<LinkedAccountsViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_linked_accounts)
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this) { connectionResult -> Timber.d("onConnectionFailed: %s", connectionResult.errorMessage) }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        facebook.clicks()
                .compose(bindToLifecycle())
                .subscribe { LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile")) }

        google.clicks()
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
