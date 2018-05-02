package com.travlog.android.apps.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseFragment;
import com.travlog.android.apps.libs.qualifiers.RequiresFragmentViewModel;
import com.travlog.android.apps.ui.activities.SignUpActivity;
import com.travlog.android.apps.viewmodels.SignUpEmailViewModel;

import butterknife.BindView;

@RequiresFragmentViewModel(SignUpEmailViewModel.class)
public class SignUpEmailFragment extends BaseFragment<SignUpEmailViewModel> {

    @BindView(R.id.email_input)
    TextInputLayout emailInputLayout;
    @BindView(R.id.email_edit)
    TextInputEditText emailEdit;

    public static @NonNull
    SignUpEmailFragment newInstance() {

        final Bundle args = new Bundle();

        final SignUpEmailFragment fragment = new SignUpEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View view = setContentView(R.layout.f_sign_up_email);

        RxTextView.textChanges(emailEdit)
                .map(CharSequence::toString)
                .compose(bindToLifecycle())
                .subscribe(((SignUpActivity) getContext()).viewModel()::email);

        return view;
    }
}
