package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.viewmodels.SplashViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SplashActivity : BaseActivity<SplashViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        viewModel?.let {
            addDisposable(
                    it.outputs.startSignInActivity()
                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe { startSignInActivity() })
                    .subscribe { startMainActivity() })

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
