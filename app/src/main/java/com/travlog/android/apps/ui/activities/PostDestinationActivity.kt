/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.savvi.rangedatepicker.CalendarPickerView
import com.travlog.android.apps.*
import com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.ui.IntentKey.DESTINATION_ID
import com.travlog.android.apps.ui.adapters.PlaceAdapter
import com.travlog.android.apps.ui.widgets.HandleableBottomSheetBehavior
import com.travlog.android.apps.viewmodels.PostDestinationViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_post_destination.*
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PostDestinationActivity : BaseActivity<PostDestinationViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var bottomSheetCalendar: HandleableBottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_post_destination)

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

        val adapter = PlaceAdapter()

        viewModel?.apply {
            location.textChanges()
                    .map { it.toString().trim() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { Timber.e(it, "location: ") }
                    .subscribe(::location)

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
                    .doOnNext {
                        // TODO: SearchLocationActivity 를 호출하지 않고 직접 텍스트를 입력받도록 하여 추가함
                        hideKeyboard()
                    }
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

            outputs.addPlace()
                    .compose(bindToLifecycle())
                    .subscribe { adapter.addData(it) }

            outputs.setSaveButtonEnabled()
                    .compose(bindToLifecycle())
                    .subscribe { setSaveButtonEnabled(it) }

            outputs.setResultAndBack()
                    .map { Intent().putExtra(DESTINATION_ID, it.id) }
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
