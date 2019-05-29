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
import androidx.databinding.DataBindingUtil
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.databinding.ADestinationDetailsBinding
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.ui.adapters.PlaceAdapter
import com.travlog.android.apps.viewmodels.DestinationDetailsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_destination_details.*
import javax.inject.Inject

class DestinationDetailsActivity : BaseActivity<DestinationDetailsViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ADestinationDetailsBinding>(this, R.layout.a_destination_details)
                .apply {
                    setSupportActionBar(toolbar)
                    setDisplayHomeAsUpEnabled(true)
                }

        val adapter = PlaceAdapter().apply {
            recycler_view.adapter = this
        }

        viewModel?.apply {
            outputs.setDestination()
                    .compose(bindToLifecycle())
                    .subscribe(binding::setDestination)

            outputs.updatePlaceData()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { adapter.clearData() }
                    .subscribe { adapter.updateData(it) }
        }
    }
}