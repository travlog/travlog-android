package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.savvi.rangedatepicker.CalendarPickerView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.ui.IntentKey
import com.travlog.android.apps.ui.IntentKey.DESTINATION
import com.travlog.android.apps.ui.IntentKey.PREDICTION
import com.travlog.android.apps.viewmodels.DestinationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_destination.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

@RequiresActivityViewModel(DestinationViewModel::class)
class DestinationActivity : BaseActivity<DestinationViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_destination)

        setSupportActionBar(toolbar)
                .apply { setDisplayHomeAsUpEnabled(true) }

        DateTime().let {
            calendar_view.init(it.minusYears(100).toDate(), it.plusYears(100).toDate(),
                    SimpleDateFormat("MMMM, YYYY", Locale.getDefault()))
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withHighlightedDate(it.toDate())

            calendar_view.scrollToDate(it.toDate())
        }


        viewModel?.apply {
            calendar_view.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

                override fun onDateSelected(date: Date?) {
                    if (start_date.text.isEmpty()) {
                        inputs.startDate(date)
                    } else {
                        inputs.endDate(date)
                    }
                }

                override fun onDateUnselected(date: Date?) {
                    inputs.startDate(null)
                    inputs.endDate(null)
                }
            })

            RxView.clicks(location_button)
                    .compose(bindToLifecycle())
                    .subscribe { showSearchLocationActivity() }

            outputs.setLocationText()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { setLocationText(it) }

            outputs.setStartDateText()
                    .compose(bindToLifecycle())
                    .subscribe { setStartDateText(it) }

            outputs.setEndDateText()
                    .compose(bindToLifecycle())
                    .subscribe { setEndDateText(it) }

            outputs.setResultAndBack()
                    .map { Intent().putExtra(DESTINATION, it as Parcelable) }
                    .doOnNext { setResult(RESULT_OK, it) }
                    .compose(bindToLifecycle())
                    .subscribe { back() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean =
            menuInflater.inflate(R.menu.menu_save, menu).run {
                super.onCreateOptionsMenu(menu)
            }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when {
                item.itemId == R.id.menu_save -> {
                    viewModel?.inputs?.saveClick()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun setLocationText(location: String) {
        location_button.text = location
    }

    private fun showSearchLocationActivity() =
            Intent(this, SearchLocationActivity::class.java).let {
                startActivityForResult(it, SEARCH_LOCATION)
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
            }

    private fun setStartDateText(startDate: String) {
        start_date.text = startDate
    }

    private fun setEndDateText(endDate: String) {
        end_date.text = endDate
    }

    override fun exitTransition(): Pair<Int, Int>? = slideInFromLeft()
}
