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
import com.travlog.android.apps.models.Place
import com.travlog.android.apps.ui.activities.PostPlaceActivity
import com.travlog.android.apps.viewmodels.errors.PostPlaceViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.PostPlaceViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.PostPlaceViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@SuppressLint("CheckResult")
class PostPlaceViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<PostPlaceActivity>(environment), PostPlaceViewModelInputs,
        PostPlaceViewModelOutputs, PostPlaceViewModelErrors {

    private val place = PublishSubject.create<String>()
    private val saveClick = PublishSubject.create<Any>()

    private val setSaveButtonEnabled = BehaviorSubject.create<Boolean>()
    private val setResultAndBack = BehaviorSubject.create<Place>()

    val inputs: PostPlaceViewModelInputs = this
    val outputs: PostPlaceViewModelOutputs = this

    init {
        place.map { it.isNotEmpty() }
                .compose(bindToLifecycle())
                .subscribe(setSaveButtonEnabled)

        place
                .compose<String>(takeWhen(saveClick))
                .map {
                    Place().apply {
                        id = RealmHelper.getAllPlaces(this@PostPlaceViewModel.realm).size.toString()
                        name = it
                    }
                }
                .doOnNext { RealmHelper.savePlaceAsync(it) }
                .compose(bindToLifecycle())
                .subscribe(setResultAndBack)
    }

    override fun place(place: String) = this.place.onNext(place)
    override fun saveClick() = saveClick.onNext(0)

    override fun setSaveButtonEnabled(): Observable<Boolean> = setSaveButtonEnabled
    override fun setResultAndBack(): Observable<Place> = setResultAndBack
}