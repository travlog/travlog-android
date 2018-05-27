package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.AppCompatButton
import android.util.Pair
import android.view.View
import butterknife.BindView
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager
import com.jakewharton.rxbinding2.view.RxView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.ui.adapters.SignUpPagerAdapter
import com.travlog.android.apps.ui.widgets.NonSwipeableViewPager
import com.travlog.android.apps.viewmodels.SignUpViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_sign_up.*

@RequiresActivityViewModel(SignUpViewModel::class)
class SignUpActivity : BaseActivity<SignUpViewModel>() {

    private var adapter: SignUpPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_sign_up)

        adapter = SignUpPagerAdapter(supportFragmentManager)
        this.view_pager.adapter = adapter

        RxView.clicks(this.close_button)
                .compose(bindToLifecycle())
                .subscribe { this.back() }

        RxViewPager.pageSelections(this.view_pager)
                .doOnNext { this.setNextButtonEnabled(false) }
                .filter { it == adapter!!.count - 1 }
                .map { R.string.done }
                .compose(bindToLifecycle())
                .subscribe(this::setNextButtonText)

        RxView.clicks(this.next)
                .map { this.view_pager.currentItem + 1 }
                .compose(bindToLifecycle())
                .subscribe { nextPosition ->
                    if (nextPosition == adapter!!.count) {
                        viewModel!!.inputs.signUpClick()
                    } else {
                        this.view_pager.currentItem = nextPosition!!
                    }
                }

        viewModel!!.outputs.setNextButtonEnabled()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setNextButtonEnabled)

        addDisposable(
                viewModel!!.outputs.signUpSuccess()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { this.back() })
    }

    private fun setNextButtonText(@StringRes resId: Int) {
        this.next.setText(resId)
    }

    private fun setNextButtonEnabled(enabled: Boolean) {
        this.next.isEnabled = enabled
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return Pair.create(android.R.anim.fade_in, R.anim.slide_out_bottom)
    }
}
