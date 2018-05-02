package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.FragmentViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.ui.fragments.SignUpPasswordFragment;
import com.travlog.android.apps.viewmodels.inputs.SignUpPasswordViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SignUpPasswordViewModelOutputs;

public class SignUpPasswordViewModel extends FragmentViewModel<SignUpPasswordFragment>
        implements SignUpPasswordViewModelInputs, SignUpPasswordViewModelOutputs {

    public SignUpPasswordViewModel(final @NonNull Environment environment) {
        super(environment);
    }

    public final SignUpPasswordViewModelInputs inputs = this;
    public final SignUpPasswordViewModelOutputs outputs = this;
}
