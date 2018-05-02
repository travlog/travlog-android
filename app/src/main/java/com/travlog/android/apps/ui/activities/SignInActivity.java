package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.SignInViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE;
import static com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_UP_FLOW;

@RequiresActivityViewModel(SignInViewModel.class)
public class SignInActivity extends BaseActivity<SignInViewModel> {

    @BindView(R.id.close_button)
    View closeButton;
    @BindView(R.id.sign_in_with_facebook_button)
    LoginButton signInFacebookButton;
    @BindView(R.id.sign_in_with_google_button)
    SignInButton signInGoogleButton;
    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.forgot_password_button)
    Button forgotPasswordButton;
    @BindView(R.id.sign_up_button)
    Button signUpButton;

    GoogleApiClient googleApiClient;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_sign_in);

        signInFacebookButton.setReadPermissions("email", "public_profile");

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,
                        connectionResult -> Timber.d("onConnectionFailed: %s", connectionResult.getErrorMessage()))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        RxView.clicks(closeButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> back());

        RxView.clicks(signInGoogleButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> startSignInWithGoogleActivity());

        RxTextView.textChanges(emailEditText)
                .map(CharSequence::toString)
                .compose(bindToLifecycle())
                .subscribe(viewModel::email);

        RxTextView.textChanges(passwordEditText)
                .map(CharSequence::toString)
                .compose(bindToLifecycle())
                .subscribe(viewModel::password);

        RxView.clicks(signInButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> viewModel.signInClick());

        RxView.clicks(signUpButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> startSignUp());

        viewModel.outputs.signInSuccess()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(__ -> this.back());

        viewModel.outputs.setSignInButtonEnabled()
                .compose(bindToLifecycle())
                .subscribe(this::setSignInButtonEnabled);
    }

    private void startSignInWithGoogleActivity() {
        final Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, SIGN_IN_WITH_GOOGLE);
    }

    private void startSignUp() {
        final Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, SIGN_UP_FLOW);
    }

    private void setSignInButtonEnabled(final boolean enabled) {
        signInButton.setEnabled(enabled);
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return Pair.create(android.R.anim.fade_in, R.anim.slide_out_bottom);
    }
}
