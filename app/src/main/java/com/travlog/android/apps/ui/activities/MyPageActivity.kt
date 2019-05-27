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
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.request.RequestOptions.circleCropTransform
import com.jakewharton.rxbinding3.view.clicks
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.glide.GlideApp
import com.travlog.android.apps.viewmodels.MyPageViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_my_page.*
import javax.inject.Inject

class MyPageActivity : BaseActivity<MyPageViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_my_page)
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        username.clicks()
                .compose(bindToLifecycle())
                .subscribe { this.startSetUserNameActivity() }

        viewModel?.apply {
            outputs.setProfilePicture()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ setProfilePicture(it) })

            outputs.setUsernameText()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ setUsernameText(it) })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            menuInflater.inflate(R.menu.menu_settings, menu).run {
                return super.onCreateOptionsMenu(menu)
            }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                startSettingsActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setProfilePicture(profilePicture: String) =
            GlideApp.with(this)
                    .load(profilePicture)
                    .apply(circleCropTransform())
                    .into(this.profile_picture)

    private fun setUsernameText(username: String) {
        this.username.text = username
    }

    private fun startSettingsActivity() =
            Intent(this, SettingsActivity::class.java).let {
                startActivityWithTransition(it, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
            }

    private fun startSetUserNameActivity() =
            Intent(this, SetUsernameActivity::class.java).let {
                startActivityWithTransition(it, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
            }
}
