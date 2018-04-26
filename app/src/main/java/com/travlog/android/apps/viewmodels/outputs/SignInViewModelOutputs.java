package com.travlog.android.apps.viewmodels.outputs;

import io.reactivex.Observable;

public interface SignInViewModelOutputs {

    /**
     * Emits a boolean to determine whether or not the login button should be enabled.
     */

    Observable<Void> signInSuccess();

    Observable<String> showFacebookAuthorizationErrorDialog();

    Observable<Boolean> setSignInButtonEnabled();
}
