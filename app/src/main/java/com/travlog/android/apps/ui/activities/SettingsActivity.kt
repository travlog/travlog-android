package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import com.jakewharton.rxbinding2.view.RxView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.a_settings.*

@RequiresActivityViewModel(SettingsViewModel::class)
class SettingsActivity : BaseActivity<SettingsViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_settings)
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        RxView.clicks(this.linked_accounts)
                .compose(bindToLifecycle())
                .subscribe { this.startLinkedAccountsActivity() }
    }

    private fun startLinkedAccountsActivity() {
        val intent = Intent(this, LinkedAccountsActivity::class.java)
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
