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

import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.activities.SearchLocationActivity
import com.travlog.android.apps.viewmodels.errors.SearchLocationViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.SearchLocationViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.SearchLocationViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchLocationViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<SearchLocationActivity>(environment),
        SearchLocationViewModelInputs, SearchLocationViewModelOutputs, SearchLocationViewModelErrors {

    private val apiClient: ApiClientType = environment.apiClient

    private val query = PublishSubject.create<String>()
    private val prediction = PublishSubject.create<Prediction>()

    private val swapSuggestions = BehaviorSubject.create<List<Prediction>>()
    private val setResultAndBack = BehaviorSubject.create<Prediction>()

    val inputs: SearchLocationViewModelInputs = this
    val outputs: SearchLocationViewModelOutputs = this
    val errors: SearchLocationViewModelErrors = this

    init {
        query
                .debounce(200, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .switchMap {
                    this.search(it)
                            .doOnSubscribe {}
                            .doAfterTerminate {}
                }
                .compose(bindToLifecycle())
                .subscribe(swapSuggestions)

        prediction
                .compose(bindToLifecycle())
                .subscribe(setResultAndBack)
    }

    private fun search(query: String): Observable<List<Prediction>> {
        return apiClient.searchLocation(query)
                .compose(neverApiError())
                .compose(neverError())
                .toObservable()
    }

    override fun query(q: String) {
        this.query.onNext(q)
    }

    override fun predictionClick(prediction: Prediction) {
        this.prediction.onNext(prediction)
    }

    override fun swapSuggestions(): Observable<List<Prediction>> {
        return swapSuggestions
    }

    override fun setResultAndBack(): Observable<Prediction> {
        return setResultAndBack
    }
}
