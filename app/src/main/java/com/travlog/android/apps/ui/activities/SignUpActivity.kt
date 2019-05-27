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
import androidx.annotation.StringRes
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.viewpager.pageSelections
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.ui.adapters.SignUpPagerAdapter
import com.travlog.android.apps.viewmodels.SignUpViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_sign_up.*
import javax.inject.Inject

class SignUpActivity : BaseActivity<SignUpViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_sign_up)

        val adapter = SignUpPagerAdapter(supportFragmentManager)
        this.view_pager.adapter = adapter

        close_button.clicks()
                .compose(bindToLifecycle())
                .subscribe { back() }

        view_pager.pageSelections()
                .doOnNext { this.setNextButtonEnabled(false) }
                .filter { it == adapter.count - 1 }
                .map { R.string.done }
                .compose(bindToLifecycle())
                .subscribe { setNextButtonText(it) }

        viewModel?.apply {
            next.clicks()
                    .map { view_pager.currentItem + 1 }
                    .compose(bindToLifecycle())
                    .subscribe {
                        when (it) {
                            adapter.count -> inputs.signUpClick()
                            else -> view_pager.currentItem = it
                        }
                    }

            outputs.setNextButtonEnabled()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { setNextButtonEnabled(it) }

            addDisposable(
                    outputs.signUpSuccess()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { back() }
            )
        }
    }

    private fun setNextButtonText(@StringRes resId: Int) = next.setText(resId)

    private fun setNextButtonEnabled(enabled: Boolean) {
        next.isEnabled = enabled
    }

    override fun exitTransition(): Pair<Int, Int>? =
            Pair.create(android.R.anim.fade_in, R.anim.slide_out_bottom)
}
