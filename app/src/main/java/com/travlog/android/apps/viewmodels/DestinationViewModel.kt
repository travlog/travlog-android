package com.travlog.android.apps.viewmodels

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.db.realm.RealmHelper
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.libs.utils.DateTimeUtils
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Location
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.ui.IntentKey.PREDICTION
import com.travlog.android.apps.ui.activities.DestinationActivity
import com.travlog.android.apps.viewmodels.errors.DestinationViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.DestinationViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.DestinationViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject

@SuppressLint("CheckResult")
class DestinationViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<DestinationActivity>(environment),
        DestinationViewModelInputs, DestinationViewModelOutputs, DestinationViewModelErrors {

    private val location = PublishSubject.create<String>()
    private val startDate = PublishSubject.create<Optional<Date?>>()
    private val endDate = PublishSubject.create<Optional<Date?>>()
    private val saveClick = PublishSubject.create<Optional<Any>>()

    private val setLocationText = BehaviorSubject.create<String>()
    private val setStartDateText = BehaviorSubject.create<String>()
    private val setEndDateText = BehaviorSubject.create<String>()
    private val setSaveButtonEnabled = BehaviorSubject.create<Boolean>()
    private val setResultAndBack = BehaviorSubject.create<Destination>()

    val inputs: DestinationViewModelInputs = this
    val outputs: DestinationViewModelOutputs = this
    val errors: DestinationViewModelErrors = this

    init {
        val predictionIntent: Observable<Prediction> = activityResult()
                .filter { it.requestCode == SEARCH_LOCATION }
                .filter { it.resultCode == RESULT_OK }
                .map { it.intent?.getParcelableExtra(PREDICTION) as Prediction }
                .doOnNext { setSaveButtonEnabled.onNext(true) }

        predictionIntent
                .map { it.structuredFormatting.mainText }
                .compose(bindToLifecycle())
                .subscribe(setLocationText)

        startDate
                .map {
                    when {
                        it.isEmpty -> ""
                        else -> DateTimeUtils.mediumDate(DateTime(it.get()))
                    }
                }
                .compose(bindToLifecycle())
                .subscribe(setStartDateText)

        endDate
                .map {
                    when {
                        it.isEmpty -> ""
                        else -> DateTimeUtils.mediumDate(DateTime(it.get()))
                    }
                }
                .compose(bindToLifecycle())
                .subscribe(setEndDateText)

        val destination = Destination()
        Observable.merge(
                location
                        .doOnNext { setSaveButtonEnabled.onNext(it.isNotEmpty()) }
                        .map { location ->
                            destination.location = Location().apply {
                                name = location
                            }
                            destination

                        },
                predictionIntent
                        .map {
                            val location = Location()
                            location.placeId = it.placeId
                            location.name = it.structuredFormatting.mainText
                            destination.location = location
                            destination
                        },
                startDate
                        .map {
                            when {
                                it.isEmpty -> destination.startDate = null
                                else -> destination.startDate = it.get()
                            }
                            destination
                        },
                endDate
                        .map {
                            when {
                                it.isEmpty -> destination.endDate = null
                                else -> destination.endDate = it.get()
                            }
                            destination
                        })
                .compose<Destination>(takeWhen(saveClick))
                .doOnNext {
                    it.id = RealmHelper.getAllDestinationsAsync(realm).size.toString()

                    RealmHelper.saveDestinationAsync(it)
                }
                .compose(bindToLifecycle())
                .subscribe { setResultAndBack.onNext(it) }
    }

    override fun location(location: String) {
        this.location.onNext(location)
    }

    override fun startDate(date: Date?) {
        startDate.onNext(Optional(date))
    }

    override fun endDate(date: Date?) {
        endDate.onNext(Optional(date))
    }

    override fun saveClick() {
        saveClick.onNext(Optional(null))
    }

    override fun setLocationText(): Observable<String> = setLocationText

    override fun setStartDateText(): Observable<String> = setStartDateText

    override fun setEndDateText(): Observable<String> = setEndDateText

    override fun setSaveButtonEnabled(): Observable<Boolean> = setSaveButtonEnabled

    override fun setResultAndBack(): Observable<Destination> = setResultAndBack
}
