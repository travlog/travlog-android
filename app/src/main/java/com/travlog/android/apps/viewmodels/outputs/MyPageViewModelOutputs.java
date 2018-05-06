package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

public interface MyPageViewModelOutputs {

    @NonNull
    Observable<String> setProfilePicture();

    @NonNull
    Observable<String> setUsernameText();
}
