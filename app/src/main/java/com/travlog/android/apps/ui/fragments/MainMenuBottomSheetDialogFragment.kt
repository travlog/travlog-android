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
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.travlog.android.apps.R
import com.travlog.android.apps.ui.activities.MyPageActivity
import com.travlog.android.apps.ui.activities.SignInActivity
import com.travlog.android.apps.ui.activities.SignUpActivity
import kotlinx.android.synthetic.main.f_main_menu_bottom_sheet.view.*

class MainMenuBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {

        fun newInstance(): MainMenuBottomSheetDialogFragment {
            val args = Bundle()

            MainMenuBottomSheetDialogFragment()
                    .let {
                        it.arguments = args
                        return it
                    }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.f_main_menu_bottom_sheet, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.sign_in.setOnClickListener {
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out)
            dismiss()
        }

        view.sign_up.setOnClickListener {
            val intent = Intent(context, SignUpActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out)
            dismiss()
        }

        view.my_page.setOnClickListener {
            val intent = Intent(context, MyPageActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out)
            dismiss()
        }
    }
}
