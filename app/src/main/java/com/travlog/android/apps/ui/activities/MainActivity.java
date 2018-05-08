package com.travlog.android.apps.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.MainViewModel;

import butterknife.BindView;

@RequiresActivityViewModel(MainViewModel.class)
public class MainActivity extends BaseActivity<MainViewModel> {

    @BindView(R.id.sign_in)
    View signInButton;
    @BindView(R.id.sign_up)
    View signUpButton;
    @BindView(R.id.my_page)
    View myPageButton;
    @BindView(R.id.add_note)
    View addNoteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);

        RxView.clicks(signInButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, SignInActivity.class)));

        RxView.clicks(signUpButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, SignUpActivity.class)));

        RxView.clicks(myPageButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, MyPageActivity.class)));

        RxView.clicks(addNoteButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, PostNoteActivity.class)));
    }
}
