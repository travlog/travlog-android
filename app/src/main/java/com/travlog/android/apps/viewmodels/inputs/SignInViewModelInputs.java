package com.travlog.android.apps.viewmodels.inputs;

import android.support.annotation.NonNull;

public interface SignInViewModelInputs {

    void loginId(@NonNull String loginId);

    void signInClick();

    void password(String password);
}
