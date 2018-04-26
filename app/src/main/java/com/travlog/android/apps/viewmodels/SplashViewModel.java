package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.ui.activities.SplashActivity;

public class SplashViewModel extends ActivityViewModel<SplashActivity> {

    public SplashViewModel(final @NonNull Environment environment) {
        super(environment);
    }
}
