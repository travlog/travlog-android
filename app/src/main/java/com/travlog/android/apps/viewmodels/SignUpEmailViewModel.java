package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.FragmentViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.ui.fragments.SignUpEmailFragment;
import com.travlog.android.apps.viewmodels.inputs.SignUpEmailViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SignUpEmailViewModelOutputs;

public class SignUpEmailViewModel extends FragmentViewModel<SignUpEmailFragment>
        implements SignUpEmailViewModelInputs, SignUpEmailViewModelOutputs {

    public SignUpEmailViewModel(final @NonNull Environment environment) {
        super(environment);
    }

    public final SignUpEmailViewModelInputs inputs = this;
    public final SignUpEmailViewModelOutputs outputs = this;
}
