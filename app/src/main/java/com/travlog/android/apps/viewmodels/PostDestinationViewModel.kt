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

package com.travlog.android.apps.viewmodels

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import com.travlog.android.apps.libs.ActivityRequestCodes.PLACE
import com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.db.realm.RealmHelper
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.libs.utils.DateTimeUtils
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Location
import com.travlog.android.apps.models.Place
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.ui.IntentKey.PLACE_ID
import com.travlog.android.apps.ui.IntentKey.PREDICTION
import com.travlog.android.apps.ui.activities.PostDestinationActivity
import com.travlog.android.apps.viewmodels.errors.PostDestinationViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.PostDestinationViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.PostDestinationViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject

@SuppressLint("CheckResult")
class PostDestinationViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<PostDestinationActivity>(environment),
        PostDestinationViewModelInputs, PostDestinationViewModelOutputs, PostDestinationViewModelErrors {

    private val location = PublishSubject.create<String>()
    private val startDate = PublishSubject.create<Optional<Date?>>()
    private val endDate = PublishSubject.create<Optional<Date?>>()
    private val saveClick = PublishSubject.create<Any>()

    private val setLocationText = BehaviorSubject.create<String>()
    private val setStartDateText = BehaviorSubject.create<String>()
    private val setEndDateText = BehaviorSubject.create<String>()
    private val addPlace = BehaviorSubject.create<Place>()
    private val setSaveButtonEnabled = BehaviorSubject.create<Boolean>()
    private val setResultAndBack = BehaviorSubject.create<Destination>()

    val inputs: PostDestinationViewModelInputs = this
    val outputs: PostDestinationViewModelOutputs = this
    val errors: PostDestinationViewModelErrors = this

    init {
        val predictionIntent: Observable<Prediction> = activityResult()
                .filter { it.requestCode == SEARCH_LOCATION && it.resultCode == RESULT_OK }
                .map { it.intent?.getParcelableExtra(PREDICTION) as Prediction }
                .doOnNext { setSaveButtonEnabled.onNext(true) }

        val placeObservable = activityResult()
                .filter { it.requestCode == PLACE && it.resultCode == RESULT_OK }
                .map { it.intent?.getStringExtra(PLACE_ID) ?: "" }
                .switchMap {
                    RealmHelper.getPlace(realm, it)
                            ?.asFlowable<Place>()
                            ?.firstElement()
                            ?.toObservable()
                }

        predictionIntent
                .map { it.structuredFormatting.mainText }
                .compose(bindToLifecycle())
                .subscribe(setLocationText)

        placeObservable
                .compose(bindToLifecycle())
                .subscribe(addPlace)

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

        location.map { it.isNotEmpty() }
                .compose(bindToLifecycle())
                .subscribe(setSaveButtonEnabled)

        val destination = Destination()

        Observable.merge(
                location.doOnNext {
                    destination.location = Location().apply {
                        id = RealmHelper.getAllLocations(this@PostDestinationViewModel.realm).size.toString()
                        name = it
                    }
                },
                predictionIntent.doOnNext {
                    val location = Location()
                    location.placeId = it.placeId
                    location.name = it.structuredFormatting.mainText
                    destination.location = location
                },
                startDate.doOnNext {
                    when {
                        it.isEmpty -> destination.startDate = null
                        else -> destination.startDate = it.get()
                    }
                },
                endDate.doOnNext {
                    when {
                        it.isEmpty -> destination.endDate = null
                        else -> destination.endDate = it.get()
                    }
                }
        )
                .map { destination }
                .compose<Destination>(takeWhen(saveClick))
                .doOnNext {
                    it.id = RealmHelper.getAllDestinations(realm).size.toString()

                    RealmHelper.saveDestinationAsync(it)
                }
                .compose(bindToLifecycle())
                .subscribe { setResultAndBack.onNext(it) }

        placeObservable
                .compose(bindToLifecycle())
                .subscribe { destination.places.add(it) }
    }

    override fun location(location: String) = this.location.onNext(location)
    override fun startDate(date: Date?) = startDate.onNext(Optional(date))
    override fun endDate(date: Date?) = endDate.onNext(Optional(date))
    override fun saveClick() = saveClick.onNext(0)

    override fun setLocationText(): Observable<String> = setLocationText
    override fun setStartDateText(): Observable<String> = setStartDateText
    override fun setEndDateText(): Observable<String> = setEndDateText
    override fun addPlace(): Observable<Place> = addPlace
    override fun setSaveButtonEnabled(): Observable<Boolean> = setSaveButtonEnabled
    override fun setResultAndBack(): Observable<Destination> = setResultAndBack
}
