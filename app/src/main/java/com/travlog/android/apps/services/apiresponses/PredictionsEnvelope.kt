package com.travlog.android.apps.services.apiresponses

import com.travlog.android.apps.models.Prediction

data class PredictionsEnvelope(val data: Data) : Envelope() {

    data class Data(val predictions: List<Prediction>)
}