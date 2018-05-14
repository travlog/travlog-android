package com.travlog.android.apps.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.libs.utils.BundleUtilsKt;
import com.travlog.android.apps.viewmodels.SplashViewModel;

import timber.log.Timber;

@RequiresActivityViewModel(SplashViewModel.class)
public class SplashActivity extends BaseActivity<SplashViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));
        finish();

        Timber.d("onCreate: %s", BundleUtilsKt.maybeGetBundle(null, "key"));
    }
}
