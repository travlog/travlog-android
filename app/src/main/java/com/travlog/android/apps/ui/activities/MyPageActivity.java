package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.MyPageViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

@RequiresActivityViewModel(MyPageViewModel.class)
public class MyPageActivity extends BaseActivity<MyPageViewModel> {

    @BindView(R.id.username)
    TextView usernameText;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_my_page);

        RxView.clicks(usernameText)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, SetUsernameActivity.class)));

        viewModel.outputs.setUsernameText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setUsernameText);
    }

    private void setUsernameText(final @NonNull String username) {
        this.usernameText.setText(username);
    }
}
