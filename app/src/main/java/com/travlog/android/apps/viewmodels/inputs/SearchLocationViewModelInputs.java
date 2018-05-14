package com.travlog.android.apps.viewmodels.inputs;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Prediction;

public interface SearchLocationViewModelInputs {

    void query(@NonNull String q);

    void predictionClick(@NonNull Prediction prediction);
}
