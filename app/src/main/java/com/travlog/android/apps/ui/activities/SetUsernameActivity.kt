package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.util.Pair
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.slideInFromLeft
import com.travlog.android.apps.viewmodels.SetUsernameViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_set_username.*

@RequiresActivityViewModel(SetUsernameViewModel::class)
class SetUsernameActivity : BaseActivity<SetUsernameViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_set_username)
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        RxTextView.textChanges(this.username_edit)
                .map { it.toString() }
                .compose(bindToLifecycle())
                .subscribe(viewModel!!.inputs::username)

        RxView.clicks(this.done)
                .compose(bindToLifecycle())
                .subscribe { viewModel!!.doneClick() }

        viewModel!!.outputs.setDoneButtonEnabled()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setDoneButtonEnabled)

        addDisposable(
                viewModel!!.outputs.back()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { this.back() })
    }

    private fun setDoneButtonEnabled(enabled: Boolean) {
        this.done.isEnabled = enabled
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
