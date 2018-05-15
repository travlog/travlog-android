package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle

import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.*
import com.travlog.android.apps.viewmodels.SplashViewModel

import timber.log.Timber

@RequiresActivityViewModel(SplashViewModel::class)
class SplashActivity : BaseActivity<SplashViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, MainActivity::class.java))
        finish()

        Timber.d("onCreate: %s", maybeGetBundle(null, "key"))
    }
}
