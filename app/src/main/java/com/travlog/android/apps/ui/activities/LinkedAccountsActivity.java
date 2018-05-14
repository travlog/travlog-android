package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.LinkedAccountsViewModel;

import java.util.Arrays;

import butterknife.BindView;
import timber.log.Timber;

import static com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE;
import static com.travlog.android.apps.libs.utils.TransitionUtilsKt.slideInFromLeft;

@RequiresActivityViewModel(LinkedAccountsViewModel.class)
public class LinkedAccountsActivity extends BaseActivity<LinkedAccountsViewModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.facebook)
    View facebookButton;
    @BindView(R.id.google)
    View googleButton;

    private GoogleApiClient googleApiClient;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_linked_accounts);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,
                        connectionResult -> Timber.d("onConnectionFailed: %s", connectionResult.getErrorMessage()))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        RxView.clicks(facebookButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile")));

        RxView.clicks(googleButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.startSignInWithGoogleActivity());
    }

    private void startSignInWithGoogleActivity() {
        final Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, SIGN_IN_WITH_GOOGLE);
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}
