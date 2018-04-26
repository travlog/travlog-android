package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.facebook.FacebookException;
import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.CurrentUserType;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.services.apiresponses.ErrorEnvelope;
import com.travlog.android.apps.ui.activities.SignInActivity;
import com.travlog.android.apps.viewmodels.errors.SignInViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.SignInViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SignInViewModelOutputs;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class SignInViewModel extends ActivityViewModel<SignInActivity> implements
        SignInViewModelInputs, SignInViewModelOutputs, SignInViewModelErrors {

    private final ApiClientType client;
    private final CurrentUserType currentUser;

    public SignInViewModel(final @NonNull Environment environment) {
        super(environment);

        client = environment.apiClient;
        currentUser = environment.currentUser;
    }

    private final PublishSubject<String> facebookAccessToken = PublishSubject.create();
    private final PublishSubject<String> email = PublishSubject.create();
    private final PublishSubject<String> password = PublishSubject.create();
    private final PublishSubject<Void> signInClick = PublishSubject.create();
    private final PublishSubject<ErrorEnvelope> loginError = PublishSubject.create();

    private final BehaviorSubject<Void> signInSuccess = BehaviorSubject.create();
    private final BehaviorSubject<FacebookException> facebookAuthorizationError = BehaviorSubject.create();
    private final BehaviorSubject<Boolean> setSignInButtonEnabled = BehaviorSubject.create();

    public final SignInViewModelInputs inputs = this;
    public final SignInViewModelOutputs outputs = this;
    public final SignInViewModelErrors errors = this;

    @Override
    public void email(final @NonNull String email) {
        this.email.onNext(email);
    }

    @Override
    public void signInClick() {
        signInClick.onNext(null);
    }

    @Override
    public void password(@NonNull String password) {
        this.password.onNext(password);
    }

    @Override
    public @NonNull
    Observable<Void> signInSuccess() {
        return signInSuccess;
    }

    @Override
    public @NonNull
    Observable<String> showFacebookAuthorizationErrorDialog() {
        return this.facebookAuthorizationError
                .map(FacebookException::getLocalizedMessage);
    }

    @Override
    public @NonNull
    Observable<String> showFacebookInvalidAccessTokenErrorToast() {
        return this.loginError
                .filter(ErrorEnvelope::isFacebookInvalidAccessTokenError)
                .map(errorEnvelope -> errorEnvelope.errorMessage);
    }

    @Override
    public @NonNull
    Observable<String> showMissingFacebookEmailErrorToast() {
        return this.loginError
                .filter(ErrorEnvelope::isMissingFacebookEmailError)
                .map(errorEnvelope -> errorEnvelope.errorMessage);
    }

    @Override
    public @NonNull
    Observable<String> showUnauthorizedErrorDialog() {
        return this.loginError
                .filter(ErrorEnvelope::isUnauthorizedError)
                .map(errorEnvelope -> errorEnvelope.errorMessage);
    }

    @Override
    public @NonNull
    Observable<Boolean> setSignInButtonEnabled() {
        return setSignInButtonEnabled;
    }

    @Override
    public Observable<String> invalidLoginError() {
        return null;
    }

    @Override
    public Observable<String> genericLoginError() {
        return null;
    }
}
