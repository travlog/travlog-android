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
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.SetUsernameViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_set_username.*
import javax.inject.Inject

class SetUsernameActivity : BaseActivity<SetUsernameViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_set_username)
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        username_edit.textChanges()
                .map { it.toString() }
                .compose(bindToLifecycle())
                .subscribe(viewModel!!.inputs::username)

        done.clicks()
                .compose(bindToLifecycle())
                .subscribe { viewModel!!.doneClick() }

        viewModel!!.outputs.setDoneButtonEnabled()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setDoneButtonEnabled)

        addDisposable(
                viewModel!!.outputs.back()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { this.back() })
    }

    private fun setDoneButtonEnabled(enabled: Boolean) {
        this.done.isEnabled = enabled
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
