package com.travlog.android.apps.viewmodels

import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.ui.activities.SettingsActivity
import com.travlog.android.apps.viewmodels.errors.SettingsViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.SettingsViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.SettingsViewModelOutputs
import javax.inject.Inject

class SettingsViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<SettingsActivity>(environment), SettingsViewModelInputs, SettingsViewModelOutputs, SettingsViewModelErrors {

    val inputs: SettingsViewModelInputs = this
    val outputs: SettingsViewModelOutputs = this
    val errors: SettingsViewModelErrors = this
}
