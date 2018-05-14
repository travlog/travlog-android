package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

public interface DestinationViewModelOutputs {

    @NonNull
    Observable<String> setLocationText();
}
