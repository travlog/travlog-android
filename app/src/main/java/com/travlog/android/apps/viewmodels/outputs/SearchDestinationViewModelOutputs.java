package com.travlog.android.apps.viewmodels.outputs;

import com.travlog.android.apps.models.Prediction;

import java.util.List;

import rx.Observable;

public interface SearchDestinationViewModelOutputs {

    Observable<List<Prediction>> swapSuggestions();

    Observable<Void> showSearchProgress();

    Observable<Void> hideSearchProgress();
}
