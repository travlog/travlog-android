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

class SearchLocationViewModel(environment: Environment) : ActivityViewModel<SearchLocationActivity>(environment),
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
