package com.travlog.android.apps.viewmodels

import com.travlog.android.apps.FragmentViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.ui.fragments.SignUpPasswordFragment
import com.travlog.android.apps.viewmodels.inputs.SignUpPasswordViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.SignUpPasswordViewModelOutputs

class SignUpPasswordViewModel(environment: Environment) : FragmentViewModel<SignUpPasswordFragment>(environment), SignUpPasswordViewModelInputs, SignUpPasswordViewModelOutputs {

    val inputs: SignUpPasswordViewModelInputs = this
    val outputs: SignUpPasswordViewModelOutputs = this
}
