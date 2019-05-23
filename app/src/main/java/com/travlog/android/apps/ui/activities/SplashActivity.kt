package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.viewmodels.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

class SplashActivity : BaseActivity<SplashViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel?.let {
            addDisposable(
                    it.outputs.startSignInActivity()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { startSignInActivity() })

            addDisposable(
                    it.outputs.startMainActivity()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { startMainActivity() }
            )
        }
    }

    private fun startSignInActivity() =
            Intent(this, SignInActivity::class.java).let {
                startActivity(it)
                back()
            }

    private fun startMainActivity() =
            Intent(this, MainActivity::class.java).let {
                startActivity(it)
                back()
            }
}
