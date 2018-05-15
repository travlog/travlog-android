package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.request.RequestOptions.circleCropTransform
import com.jakewharton.rxbinding2.view.RxView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.glide.GlideApp
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.viewmodels.MyPageViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_my_page.*

@RequiresActivityViewModel(MyPageViewModel::class)
class MyPageActivity : BaseActivity<MyPageViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_my_page)

        RxView.clicks(this.settings)
                .compose(bindToLifecycle())
                .subscribe { this.startSettingsActivity() }

        RxView.clicks(this.username)
                .compose(bindToLifecycle())
                .subscribe { this.startSetUserNameActivity() }

        viewModel!!.outputs.setProfilePicture()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.setProfilePicture(it) })

        viewModel!!.outputs.setUsernameText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.setUsernameText(it) })
    }

    private fun setProfilePicture(profilePicture: String) {
        GlideApp.with(this)
                .load(profilePicture)
                .apply(circleCropTransform())
                .into(this.profile_picture)
    }

    private fun setUsernameText(username: String) {
        this.username.text = username
    }

    private fun startSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }

    private fun startSetUserNameActivity() {
        val intent = Intent(this, SetUsernameActivity::class.java)
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }
}
