package com.travlog.android.apps.viewmodels.outputs;

import rx.Observable;

public interface SettingsViewModelOutputs {

    Observable<Boolean> setSignInButtonGone();

    Observable<Boolean> setSignOutButtonGone();

    Observable<Boolean> setProfileContainerGone();

    Observable<String> setNameText();

    Observable<String> setEmailText();
}
