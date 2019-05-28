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
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding3.widget.textChanges
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.EditNoteViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_post_note.*
import javax.inject.Inject

class EditNoteActivity : BaseActivity<EditNoteViewModel>() {

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
            title_edit.textChanges()
                    .skip(1)
                    .map { it.toString() }
                    .compose(bindToLifecycle())
                    .subscribe(inputs::title)

            outputs.setTitleText()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this@EditNoteActivity::setTitleText)

            addDisposable(
                    outputs.back()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { this@EditNoteActivity.back() })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean =
            menuInflater.inflate(R.menu.menu_save, menu)
                    .run {
                        super.onCreateOptionsMenu(menu)
                    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            viewModel!!.inputs.saveClick()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTitleText(title: String) =
            title_edit.setText(title).apply {
                title_edit.setSelection(title.length)
            }

    override fun exitTransition(): Pair<Int, Int>? = slideInFromLeft()
}
