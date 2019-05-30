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
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.db.realm.RealmHelper
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Place
import com.travlog.android.apps.models.Place.Companion.FIELD_ORDER
import com.travlog.android.apps.ui.IntentKey.DESTINATION_ID
import com.travlog.android.apps.ui.activities.DestinationDetailsActivity
import com.travlog.android.apps.viewmodels.errors.DestinationDetailsViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.DestinationDetailsViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.DestinationDetailsViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@SuppressLint("CheckResult")
class DestinationDetailsViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<DestinationDetailsActivity>(environment), DestinationDetailsViewModelInputs,
        DestinationDetailsViewModelOutputs, DestinationDetailsViewModelErrors {

    private val deleteClick = PublishSubject.create<Any>()

    private val setDestination = BehaviorSubject.create<Destination>()
    private val updatePlaceData = BehaviorSubject.create<List<Place>>()
    private val finish = CompletableSubject.create()

    val inputs: DestinationDetailsViewModelInputs = this
    val outputs: DestinationDetailsViewModelOutputs = this
    val errors: DestinationDetailsViewModelErrors = this

    init {
        val destination = intent()
                .map { it.getStringExtra(DESTINATION_ID) ?: "" }
                .switchMap {
                    RealmHelper.getDestination(realm, it)
                            ?.asFlowable<Destination>()
                            ?.toObservable()
                }
                .filter { it.isLoaded && it.isValid }

        destination
                .compose(bindToLifecycle())
                .subscribe(::setDestination)

        destination
                .compose<Destination>(takeWhen(deleteClick))
                .map { it.id }
                .doOnNext(RealmHelper::deleteDestination)
                .compose(bindToLifecycle())
                .subscribe { finish.onComplete() }
    }

    private fun setDestination(destination: Destination) {
        setDestination.onNext(destination)
        updatePlaceData.onNext(destination.places.sort(FIELD_ORDER))
    }

    override fun deleteClick() = deleteClick.onNext(0)

    override fun setDestination(): Observable<Destination> = setDestination
    override fun updatePlaceData(): Observable<List<Place>> = updatePlaceData
    override fun finish(): Completable = finish
}