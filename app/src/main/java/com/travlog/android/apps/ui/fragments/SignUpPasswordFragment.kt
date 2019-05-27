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

package com.travlog.android.apps.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.widget.textChanges
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseFragment
import com.travlog.android.apps.ui.activities.SignUpActivity
import com.travlog.android.apps.viewmodels.SignUpPasswordViewModel
import kotlinx.android.synthetic.main.f_sign_up_password.*

class SignUpPasswordFragment : BaseFragment<SignUpPasswordViewModel>() {

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return LayoutInflater.from(context).inflate(R.layout.f_sign_up_password, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        password_edit.textChanges()
                .map<String> { it.toString() }
                .compose(bindToLifecycle())
                .subscribe({ (context as SignUpActivity).viewModel()!!.password(it) })
    }

    companion object {

        fun newInstance(): SignUpPasswordFragment {

            val args = Bundle()

            val fragment = SignUpPasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
