package com.travlog.android.apps.viewmodels.inputs

import com.travlog.android.apps.models.Prediction

interface SearchLocationViewModelInputs {

    fun query(q: String)

    fun predictionClick(prediction: Prediction)
}
