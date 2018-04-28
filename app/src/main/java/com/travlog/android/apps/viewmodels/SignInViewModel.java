package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import timber.log.Timber;

public class SignInViewModel extends ActivityViewModel<SignInActivity> implements
        SignInViewModelInputs, SignInViewModelOutputs, SignInViewModelErrors {

    private final ApiClientType client;
    private final CurrentUserType currentUser;
    private final CallbackManager callbackManager;

    public SignInViewModel(final @NonNull Environment environment) {
        super(environment);

        client = environment.apiClient;
        currentUser = environment.currentUser;
        callbackManager = CallbackManager.Factory.create();

        activityResult()
                .compose(bindToLifecycle())
                .subscribe(activityResult -> {
                    Timber.d("SignInViewModel: activityResult? %s", activityResult);
                    callbackManager.onActivityResult(activityResult.requestCode, activityResult.resultCode, activityResult.intent);
                });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("facebook onSuccess: %s", loginResult.getAccessToken().getToken());

                facebookAccessToken.onNext(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Timber.d("facebook onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Timber.w(error.getCause(), "facebook onError: %s" + error.toString());
                facebookAuthorizationError.onNext(error);
            }
        });
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
