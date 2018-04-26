package com.travlog.android.apps.viewmodels.outputs;

public interface SignUpViewModelInputs {

    void email(String email);

    void password(String password);

    void confirmPassword(String password);

    void name(String name);

    void signUpClick();
}
