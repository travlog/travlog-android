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

import android.content.Intent
import android.os.Bundle
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.viewmodels.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SplashActivity : BaseActivity<SplashViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        viewModel?.let {
            addDisposable(
                    it.outputs.startSignInActivity()
                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe { startSignInActivity() })
                    .subscribe { startMainActivity() })

            addDisposable(
                    it.outputs.startMainActivity()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { startMainActivity() }
            )
        }
    }

    private fun startSignInActivity() =
            Intent(this, SignInActivity::class.java).let {
                startActivity(it)
                back()
            }

    private fun startMainActivity() =
            Intent(this, MainActivity::class.java).let {
                startActivity(it)
                back()
            }
}
