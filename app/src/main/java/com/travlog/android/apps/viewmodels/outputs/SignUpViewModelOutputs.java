package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface SignUpViewModelOutputs {

    @NonNull
    Observable<Boolean> setNextButtonEnabled();

    @NonNull
    Completable signUpSuccess();
}
