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
