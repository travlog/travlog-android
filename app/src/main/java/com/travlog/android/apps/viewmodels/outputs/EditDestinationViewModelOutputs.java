package com.travlog.android.apps.viewmodels.outputs;

import android.util.Pair;

import com.travlog.android.apps.models.Destination;
import com.travlog.android.apps.models.Prediction;

import rx.Observable;

public interface EditDestinationViewModelOutputs {

    Observable<Integer> setAddDestinationButtonVisibility();

    Observable<Destination> addDestinationView();

    Observable<Pair<Integer, Prediction>> setPrediction();
}
