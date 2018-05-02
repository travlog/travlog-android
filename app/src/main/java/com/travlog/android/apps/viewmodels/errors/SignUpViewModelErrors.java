package com.travlog.android.apps.viewmodels.errors;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

public interface SignUpViewModelErrors {

    @NonNull
    Observable<String> duplicatedError();
}
