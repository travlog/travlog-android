package com.travlog.android.apps.viewmodels.errors;

import io.reactivex.Observable;

public interface SignInViewModelErrors {

    Observable<String> invalidLoginError();

    Observable<String> genericLoginError();

    Observable<String> showFacebookAuthorizationErrorDialog();

    Observable<String> showFacebookInvalidAccessTokenErrorToast();

    Observable<String> showMissingFacebookEmailErrorToast();

    Observable<String> showUnauthorizedErrorDialog();
}
