package com.travlog.android.apps.viewmodels.outputs;

import rx.Observable;

public interface ResetPasswordViewModelOutputs {

    Observable<Boolean> setResetPasswordButtonEnabled();

    Observable<Void> finishWithSuccess();
}
