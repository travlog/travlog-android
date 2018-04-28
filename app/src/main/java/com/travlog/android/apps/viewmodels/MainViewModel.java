package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.ui.activities.MainActivity;
import com.travlog.android.apps.viewmodels.errors.MainViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.MainViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.MainViewModelOutputs;

public class MainViewModel extends ActivityViewModel<MainActivity>
        implements MainViewModelInputs, MainViewModelOutputs, MainViewModelErrors {

    public MainViewModel(@NonNull Environment environment) {
        super(environment);
    }

    public final MainViewModelInputs inputs = this;
    public final MainViewModelOutputs outputs = this;
    public final MainViewModelErrors errors = this;
}
