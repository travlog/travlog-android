package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.SetUsernameViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

@RequiresActivityViewModel(SetUsernameViewModel.class)
public class SetUsernameActivity extends BaseActivity<SetUsernameViewModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.username_input)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.username_edit)
    TextInputEditText usernameEdit;
    @BindView(R.id.save_button)
    View saveButton;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_set_username);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        RxTextView.textChanges(usernameEdit)
                .map(CharSequence::toString)
                .compose(bindToLifecycle())
                .subscribe(viewModel.inputs::username);

        RxView.clicks(saveButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> viewModel.saveClick());

        viewModel.outputs.setSaveButtonEnabled()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setSaveButtonEnabled);

        viewModel.outputs.back()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::back);
    }

    private void setSaveButtonEnabled(final boolean enabled) {
        this.saveButton.setEnabled(enabled);
    }
}
