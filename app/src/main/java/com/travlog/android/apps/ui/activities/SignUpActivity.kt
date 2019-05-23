package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.util.Pair
import androidx.annotation.StringRes
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.viewpager.pageSelections
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.ui.adapters.SignUpPagerAdapter
import com.travlog.android.apps.viewmodels.SignUpViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_sign_up.*

class SignUpActivity : BaseActivity<SignUpViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_sign_up)

        val adapter = SignUpPagerAdapter(supportFragmentManager)
        this.view_pager.adapter = adapter

        close_button.clicks()
                .compose(bindToLifecycle())
                .subscribe { back() }

        view_pager.pageSelections()
                .doOnNext { this.setNextButtonEnabled(false) }
                .filter { it == adapter.count - 1 }
                .map { R.string.done }
                .compose(bindToLifecycle())
                .subscribe { setNextButtonText(it) }

        viewModel?.apply {
            next.clicks()
                    .map { view_pager.currentItem + 1 }
                    .compose(bindToLifecycle())
                    .subscribe {
                        when (it) {
                            adapter.count -> inputs.signUpClick()
                            else -> view_pager.currentItem = it
                        }
                    }

            outputs.setNextButtonEnabled()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { setNextButtonEnabled(it) }

            addDisposable(
                    outputs.signUpSuccess()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { back() }
            )
        }
    }

    private fun setNextButtonText(@StringRes resId: Int) = next.setText(resId)

    private fun setNextButtonEnabled(enabled: Boolean) {
        next.isEnabled = enabled
    }

    override fun exitTransition(): Pair<Int, Int>? =
            Pair.create(android.R.anim.fade_in, R.anim.slide_out_bottom)
}
