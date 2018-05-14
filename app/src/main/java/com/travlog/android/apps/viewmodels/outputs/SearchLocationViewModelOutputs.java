package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Prediction;

import java.util.List;

import io.reactivex.Observable;

public interface SearchLocationViewModelOutputs {

    @NonNull
    Observable<List<Prediction>> swapSuggestions();

    @NonNull
    Observable<Prediction> setResultAndBack();
}
