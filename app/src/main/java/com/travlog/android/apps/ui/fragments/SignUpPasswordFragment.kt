package com.travlog.android.apps.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseFragment
import com.travlog.android.apps.libs.qualifiers.RequiresFragmentViewModel
import com.travlog.android.apps.ui.activities.SignUpActivity
import com.travlog.android.apps.viewmodels.SignUpPasswordViewModel
import kotlinx.android.synthetic.main.f_sign_up_password.*

@RequiresFragmentViewModel(SignUpPasswordViewModel::class)
class SignUpPasswordFragment : BaseFragment<SignUpPasswordViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val view = setContentView(R.layout.f_sign_up_password)

        RxTextView.textChanges(this.password_edit)
                .map<String> { it.toString() }
                .compose(bindToLifecycle())
                .subscribe({ (context as SignUpActivity).viewModel()!!.password(it) })

        return view
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
