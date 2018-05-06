package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.ui.activities.SettingsActivity;
import com.travlog.android.apps.viewmodels.errors.SettingsViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.SettingsViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SettingsViewModelOutputs;

public class SettingsViewModel extends ActivityViewModel<SettingsActivity>
        implements SettingsViewModelInputs, SettingsViewModelOutputs, SettingsViewModelErrors {

    public SettingsViewModel(final @NonNull Environment environment) {
        super(environment);
    }

    public final SettingsViewModelInputs inputs = this;
    public final SettingsViewModelOutputs outputs = this;
    public final SettingsViewModelErrors errors = this;
}
