package com.travlog.android.apps.viewmodels.outputs

import com.travlog.android.apps.models.Prediction

import io.reactivex.Observable

interface SearchLocationViewModelOutputs {

    fun swapSuggestions(): Observable<List<Prediction>>

    fun setResultAndBack(): Observable<Prediction>
}
