package com.travlog.android.apps.viewmodels;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.Task;
import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.CurrentUserType;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.Envelope;
import com.travlog.android.apps.ui.activities.SignInActivity;
import com.travlog.android.apps.viewmodels.errors.SignInViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.SignInViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SignInViewModelOutputs;

import org.json.JSONException;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;

public class SignInViewModel extends ActivityViewModel<SignInActivity> implements
        SignInViewModelInputs, SignInViewModelOutputs, SignInViewModelErrors {

    private final ApiClientType client;
    private final CurrentUserType currentUser;
    private final CallbackManager callbackManager;

    @SuppressLint("CheckResult")
    public SignInViewModel(final @NonNull Environment environment) {
        super(environment);

        client = environment.apiClient;
        currentUser = environment.currentUser;
        callbackManager = CallbackManager.Factory.create();

        activityResult()
                .filter(activityResult -> activityResult.requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode())
                .compose(bindToLifecycle())
                .subscribe(activityResult -> callbackManager.onActivityResult(activityResult.requestCode, activityResult.resultCode, activityResult.intent));

        activityResult()
                .filter(activityResult -> activityResult.requestCode == SIGN_IN_WITH_GOOGLE)
                .map(activityResult -> activityResult.intent)
                .map(GoogleSignIn::getSignedInAccountFromIntent)
                .map(Task::getResult)
                .map(googleSignInAccount -> {
                    final XauthBody body = new XauthBody();
                    body.userId = googleSignInAccount.getId();
                    body.name = googleSignInAccount.getDisplayName();
                    body.email = googleSignInAccount.getEmail();
                    body.type = "google";

                    return body;
                })
                .compose(bindToLifecycle())
                .subscribe(xauthBody);

        xauthBody
                .switchMap(body -> this.signup(body)
                        .doOnSubscribe(subscription -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe(envelope -> {
                    currentUser.login(envelope.data.user, envelope.data.accessToken);
                    signInSuccess.onNext(new Optional<>(null));
                });

        facebookAuthorizationError
                .compose(bindToLifecycle())
                .subscribe(this::clearFacebookSession);

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();

                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(accessToken, (obj, res) -> {

                    Timber.d("onSuccess: %s", res);

                    try {
                        final XauthBody body = new XauthBody();
                        body.userId = accessToken.getUserId();
                        body.name = obj.getString("name");
                        body.email = obj.getString("email");
                        body.type = "facebook";

                        xauthBody.onNext(body);
                    } catch (JSONException e) {
                        Timber.w(e, "onSuccess: ");
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Timber.d("facebook onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Timber.w(error.getCause(), "facebook onError: %s", error.toString());
                facebookAuthorizationError.onNext(error);
            }
        });

        duplicatedError()
                .compose(bindToLifecycle())
                .subscribe(msg -> Timber.d("SignInViewModel: %s", msg));
    }

    private void clearFacebookSession(final @NonNull FacebookException e) {
        LoginManager.getInstance().logOut();
    }

    private @NonNull
    Observable<AccessTokenEnvelope> signup(final @NonNull XauthBody body) {
        return client.signup(body)
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<XauthBody> xauthBody = PublishSubject.create();
    private final PublishSubject<String> email = PublishSubject.create();
    private final PublishSubject<String> password = PublishSubject.create();
    private final PublishSubject<Optional> signInClick = PublishSubject.create();
    private final PublishSubject<Envelope> loginError = PublishSubject.create();

    private final BehaviorSubject<Optional> signInSuccess = BehaviorSubject.create();
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
        signInClick.onNext(new Optional<>(null));
    }

    @Override
    public void password(@NonNull String password) {
        this.password.onNext(password);
    }

    @Override
    public @NonNull
    Observable<Optional> signInSuccess() {
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
    Observable<Boolean> setSignInButtonEnabled() {
        return setSignInButtonEnabled;
    }

    @Override
    public Observable<String> duplicatedError() {
        return loginError.filter(Envelope::isDuplicatedUser)
                .map(errorEnvelope -> errorEnvelope.err.msg);
    }
}
