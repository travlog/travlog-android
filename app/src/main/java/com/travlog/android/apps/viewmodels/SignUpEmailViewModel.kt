/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
