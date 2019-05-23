package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.util.Pair
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.SetUsernameViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_set_username.*
import javax.inject.Inject

class SetUsernameActivity : BaseActivity<SetUsernameViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_set_username)
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        username_edit.textChanges()
                .map { it.toString() }
                .compose(bindToLifecycle())
                .subscribe(viewModel!!.inputs::username)

        done.clicks()
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
