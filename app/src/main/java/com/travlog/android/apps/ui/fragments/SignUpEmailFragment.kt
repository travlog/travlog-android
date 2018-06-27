package com.travlog.android.apps.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseFragment
import com.travlog.android.apps.libs.qualifiers.RequiresFragmentViewModel
import com.travlog.android.apps.ui.activities.SignUpActivity
import com.travlog.android.apps.viewmodels.SignUpEmailViewModel
import kotlinx.android.synthetic.main.f_sign_up_email.*

@RequiresFragmentViewModel(SignUpEmailViewModel::class)
class SignUpEmailFragment : BaseFragment<SignUpEmailViewModel>() {

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return LayoutInflater.from(context).inflate(R.layout.f_sign_up_email, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RxTextView.textChanges(email_edit.getEditText())
                .map<String> { it.toString() }
                .compose(bindToLifecycle())
                .subscribe({ (context as SignUpActivity).viewModel()!!.email(it) })
    }

    companion object {

        fun newInstance(): SignUpEmailFragment {

            val args = Bundle()

            val fragment = SignUpEmailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
