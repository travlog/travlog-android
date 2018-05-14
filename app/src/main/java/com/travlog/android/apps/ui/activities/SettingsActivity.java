package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.SettingsViewModel;

import butterknife.BindView;

import static com.travlog.android.apps.libs.utils.TransitionUtilsKt.slideInFromLeft;

@RequiresActivityViewModel(SettingsViewModel.class)
public class SettingsActivity extends BaseActivity<SettingsViewModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.linked_accounts)
    View linkedAccounts;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_settings);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        RxView.clicks(linkedAccounts)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.startLinkedAccountsActivity());
    }

    private void startLinkedAccountsActivity() {
        final Intent intent = new Intent(this, LinkedAccountsActivity.class);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}
