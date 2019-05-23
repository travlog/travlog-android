package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.request.RequestOptions.circleCropTransform
import com.jakewharton.rxbinding3.view.clicks
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.glide.GlideApp
import com.travlog.android.apps.viewmodels.MyPageViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_my_page.*

class MyPageActivity : BaseActivity<MyPageViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
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
