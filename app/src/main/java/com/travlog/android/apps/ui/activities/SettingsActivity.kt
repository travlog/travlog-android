package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import com.jakewharton.rxbinding3.view.clicks
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.a_settings.*
import javax.inject.Inject

class SettingsActivity : BaseActivity<SettingsViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_settings)
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        linked_accounts.clicks()
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
