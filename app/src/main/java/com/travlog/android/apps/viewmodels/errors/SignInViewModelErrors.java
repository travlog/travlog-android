package com.travlog.android.apps.viewmodels.errors;

import io.reactivex.Observable;

public interface SignInViewModelErrors {

    Observable<String> duplicatedError();
}
