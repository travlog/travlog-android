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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.ui.IntentKey.NOTE_ID
import com.travlog.android.apps.ui.adapters.NoteAdapter
import com.travlog.android.apps.ui.fragments.MainMenuBottomSheetDialogFragment
import com.travlog.android.apps.ui.widgets.StartSnapHelper
import com.travlog.android.apps.viewmodels.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_main)

        when (recycler_view.layoutManager) {
            is LinearLayoutManager -> (recycler_view.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
        }

        StartSnapHelper().apply {
            attachToRecyclerView(recycler_view)
        }

        viewModel?.apply {
            val adapter = NoteAdapter(this)
            recycler_view.adapter = adapter

            add_note.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { showPostActivity() }

            menu.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { showMainMenuBottomSheet() }

            my_info.clicks()
                    .compose(bindToLifecycle())
                    .subscribe {
                        Intent(this@MainActivity, MyPageActivity::class.java).apply {
                            startActivityWithTransition(this, R.anim.slide_in_up, R.anim.fade_out)
                        }
                    }

            outputs.clearNotes()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapter.clearData() }

            outputs.updateData()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { adapter.clearData() }
                    .subscribe { adapter.updateData(it) }

            outputs.showNoteDetailsActivity()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { showNoteDetailsActivity(it) }

            outputs.updateNotes()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapter.updateNote(it) }

            this.loadMore()
        }
    }

    private fun showPostActivity() {
        Intent(this, PostNoteActivity::class.java).let {
            startActivityWithTransition(it, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
        }
    }

    private fun showMainMenuBottomSheet() {
        MainMenuBottomSheetDialogFragment.newInstance()
                .show(supportFragmentManager, "MainMenuBottomSheetDialogFragment")
    }

    private fun showNoteDetailsActivity(note: Note) {
        Intent(this, NoteDetailsActivity::class.java).let {
            it.putExtra(NOTE_ID, note.id)
            startActivityWithTransition(it, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
        }
    }
}
