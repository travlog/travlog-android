package com.travlog.android.apps.viewmodels

import com.travlog.android.apps.FragmentViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.ui.fragments.SignUpEmailFragment
import com.travlog.android.apps.viewmodels.inputs.SignUpEmailViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.SignUpEmailViewModelOutputs
import javax.inject.Inject

class SignUpEmailViewModel @Inject constructor(environment: Environment
) : FragmentViewModel<SignUpEmailFragment>(environment), SignUpEmailViewModelInputs, SignUpEmailViewModelOutputs {

    val inputs: SignUpEmailViewModelInputs = this
    val outputs: SignUpEmailViewModelOutputs = this
}
