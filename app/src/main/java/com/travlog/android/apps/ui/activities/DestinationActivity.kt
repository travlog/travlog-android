package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Pair
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jakewharton.rxbinding3.view.clicks
import com.savvi.rangedatepicker.CalendarPickerView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.ui.IntentKey.DESTINATION
import com.travlog.android.apps.ui.widgets.HandleableBottomSheetBehavior
import com.travlog.android.apps.viewmodels.DestinationViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_destination.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class DestinationActivity : BaseActivity<DestinationViewModel>() {

    var bottomSheetCalendar: HandleableBottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_destination)

        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)

        bottomSheetCalendar = HandleableBottomSheetBehavior.from(bottom_sheet_calenar)
        bottomSheetCalendar?.setHandleView(weekdays_container)

        val currentDateTime = DateTime()

        currentDateTime.let {
            calendar_view.init(it.minusYears(100).toDate(), it.plusYears(100).toDate(),
                    SimpleDateFormat("MMMM, YYYY", Locale.getDefault()))
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withHighlightedDate(it.toDate())

            calendar_view.scrollToDate(it.toDate())
        }

        viewModel?.apply {
            calendar_view.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

                override fun onDateSelected(date: Date?) {
                    when {
                        start_date.text.isEmpty() -> inputs.startDate(date)
                        end_date.text.isEmpty() -> inputs.endDate(date).run {
                            bottomSheetCalendar?.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }
                }

                override fun onDateUnselected(date: Date?) {
                    inputs.startDate(null)
                    inputs.endDate(null)
                }
            })

            location_button.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { showSearchLocationActivity() }


            Observable.merge(start_date.clicks(), end_date.clicks())
                    .compose(bindToLifecycle())
                    .subscribe {
                        calendar_view.scrollToDate(currentDateTime.toDate()).run {
                            bottomSheetCalendar?.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

            save_button.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { inputs.saveClick() }

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

            outputs.setSaveButtonEnabled()
                    .compose(bindToLifecycle())
                    .subscribe { setSaveButtonEnabled(it) }

            outputs.setResultAndBack()
                    .map { Intent().putExtra(DESTINATION, it as Parcelable) }
                    .doOnNext { setResult(RESULT_OK, it) }
                    .compose(bindToLifecycle())
                    .subscribe { back() }
        }
    }

    override fun onBackPressed() =
            when {
                bottomSheetCalendar?.state == BottomSheetBehavior.STATE_EXPANDED ->
                    bottomSheetCalendar?.state = BottomSheetBehavior.STATE_COLLAPSED
                else -> super.onBackPressed()
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

    private fun setSaveButtonEnabled(enabled: Boolean) {
        save_button.isEnabled = enabled
    }

    override fun exitTransition(): Pair<Int, Int>? = slideInFromLeft()
}
