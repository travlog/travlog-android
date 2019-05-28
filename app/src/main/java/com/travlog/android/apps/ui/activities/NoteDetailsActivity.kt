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
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding3.view.clicks
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.databinding.ANoteDetailsBinding
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.ui.IntentKey.NOTE_ID
import com.travlog.android.apps.ui.adapters.DestinationAdapter
import com.travlog.android.apps.viewmodels.NoteDetailsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_note_details.*
import javax.inject.Inject

class NoteDetailsActivity : BaseActivity<NoteDetailsViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var destinationAdapter: DestinationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ANoteDetailsBinding>(this, R.layout.a_note_details)
                .apply {
                    setSupportActionBar(toolbar)
                    setDisplayHomeAsUpEnabled(true)
                }

        viewModel?.apply {
            destinationAdapter = DestinationAdapter(this)
                    .apply {
                        recycler_view.adapter = this
                    }

            delete_button.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { inputs.deleteClick() }

            outputs.setNote()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(binding::setNote)

            outputs.updateDestinations()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { destinationAdapter.clearData() }
                    .subscribe { destinationAdapter.updateData(it) }

            outputs.showEditNote()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::showEditNoteActivity)

            addDisposable(
                    outputs.finish()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { back() }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.itemId.let {
            return when (it) {
                R.id.menu_edit -> {
                    viewModel?.inputs?.editClick()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showEditNoteActivity(note: Note) =
            Intent(this, EditNoteActivity::class.java).let {
                it.putExtra(NOTE_ID, note.id)
                startActivityWithTransition(it, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
            }

    override fun exitTransition(): Pair<Int, Int>? = slideInFromLeft()
}
