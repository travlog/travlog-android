package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.viewmodels.DestinationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_destination.*

@RequiresActivityViewModel(DestinationViewModel::class)
class DestinationActivity : BaseActivity<DestinationViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_destination)

        RxView.clicks(location)
                .compose(bindToLifecycle())
                .subscribe { this.showSearchLocationActivity() }

        viewModel!!.outputs.setLocationText()
                .compose(bindToLifecycle<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setLocationText)
    }

    private fun setLocationText(location: String) {
        this.location.text = location
    }

    private fun showSearchLocationActivity() {
        val intent = Intent(this, SearchLocationActivity::class.java)
        startActivityForResult(intent, SEARCH_LOCATION)
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }
}
