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
import android.util.Pair
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.ActivityRequestCodes.DESTINATION
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.ui.adapters.DestinationAdapter
import com.travlog.android.apps.viewmodels.PostNoteViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_post_note.*
import javax.inject.Inject

class PostNoteActivity : BaseActivity<PostNoteViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_post_note).apply {
            setSupportActionBar(toolbar)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel?.apply {
            val destinationsAdapter = DestinationAdapter(this)
            destination_recycler.adapter = destinationsAdapter

            title_edit.textChanges()
                    .map { it.toString().trim() }
                    .compose(bindToLifecycle())
                    .subscribe(inputs::title)

            add_destination.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { showDestinationActivity() }

            memo.textChanges()
                    .map { it.toString() }
                    .compose(bindToLifecycle())
                    .subscribe(inputs::memo)

            save_button.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { inputs.saveClick() }

            outputs.setSaveButtonEnabled()
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(bindToLifecycle())
                    .subscribe { setSaveButtonEnabled(it) }

            outputs.addDestination()
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(bindToLifecycle())
                    .subscribe { destinationsAdapter.addData(it) }

            addDisposable(
                    outputs.setResultAndBack()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { this@PostNoteActivity.back() }
            )
        }
    }

    private fun setSaveButtonEnabled(enabled: Boolean) {
        save_button.isEnabled = enabled
    }

    private fun showDestinationActivity() =
            Intent(this, PostDestinationActivity::class.java).let {
                startActivityForResult(it, DESTINATION)
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
            }

    override fun exitTransition(): Pair<Int, Int>? = slideInFromLeft()
}
