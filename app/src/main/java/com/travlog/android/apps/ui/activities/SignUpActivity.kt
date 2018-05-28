package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.support.annotation.StringRes
import android.util.Pair
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.jakewharton.rxbinding2.view.RxView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.ui.adapters.SignUpPagerAdapter
import com.travlog.android.apps.viewmodels.SignUpViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_sign_up.*

@RequiresActivityViewModel(SignUpViewModel::class)
class SignUpActivity : BaseActivity<SignUpViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_sign_up)

        val adapter = SignUpPagerAdapter(supportFragmentManager)
        this.view_pager.adapter = adapter

        RxView.clicks(close_button)
                .compose(bindToLifecycle())
                .subscribe { back() }

        RxViewPager.pageSelections(view_pager)
                .doOnNext { this.setNextButtonEnabled(false) }
                .filter { it == adapter.count - 1 }
                .map { R.string.done }
                .compose(bindToLifecycle())
                .subscribe { setNextButtonText(it) }

        viewModel?.apply {
            RxView.clicks(next)
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
