package com.travlog.android.apps.viewmodels.inputs;

import android.support.annotation.NonNull;

public interface SignUpViewModelInputs {

    void email(@NonNull String email);

    void password(@NonNull String password);

    void signUpClick();
}
