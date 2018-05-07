package com.travlog.android.apps.viewmodels;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.CurrentUserType;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.services.apirequests.OauthBody;
import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.Envelope;
import com.travlog.android.apps.ui.activities.SignInActivity;
import com.travlog.android.apps.viewmodels.errors.SignInViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.SignInViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SignInViewModelOutputs;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.combineLatestPair;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.pipeApiErrorsTo;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen;

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
                .filter(activityResult -> activityResult.resultCode == RESULT_OK)
                .map(activityResult -> activityResult.intent)
                .map(GoogleSignIn::getSignedInAccountFromIntent)
                .map(Task::getResult)
                .map(GoogleSignInAccount::getIdToken)
                .switchMap(accessToken -> this.oAuth(accessToken, "google")
                        .doOnSubscribe(disposable -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe(this::signInSuccess);

        firebaseAccessToken
                .map(accessToken -> Pair.create(accessToken, "facebook"))
                .switchMap(ap -> this.oAuth(ap.first, ap.second)
                        .doOnSubscribe(subscription -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe(this::signInSuccess);

        facebookAuthorizationError
                .compose(bindToLifecycle())
                .subscribe(this::clearFacebookSession);

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();

                SignInViewModel.this.firebaseAccessToken.onNext(accessToken.getToken());
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

        final Observable<Pair<String, String>> loginIdAndPassword = loginId.compose(combineLatestPair(password));

        final Observable<Boolean> isValid = loginIdAndPassword
                .map(ep -> this.isValid(ep.first, ep.second));

        isValid
                .compose(bindToLifecycle())
                .subscribe(setSignInButtonEnabled);

        loginIdAndPassword
                .compose(takeWhen(signInClick))
                .switchMap(ip -> this.signIn(ip.first, ip.second)
                        .doOnSubscribe(disposable -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe(this::signInSuccess);
    }

    private void signInSuccess(final @NonNull AccessTokenEnvelope accessTokenEnvelope) {
        currentUser.login(accessTokenEnvelope.data.user, accessTokenEnvelope.data.accessToken);
        signInSuccess.onNext(new Optional<>(null));
    }

    private boolean isValid(final @NonNull String loginId, final @NonNull String password) {
        return !TextUtils.isEmpty(loginId) && !TextUtils.isEmpty(password);
    }

    private void clearFacebookSession(final @NonNull FacebookException e) {
        LoginManager.getInstance().logOut();
    }

    private @NonNull
    Observable<AccessTokenEnvelope> oAuth(final @NonNull String accessToken,
                                          final @NonNull String provider) {

        final OauthBody body = new OauthBody();
        body.token = accessToken;
        body.provider = provider;

        return client.oAuth(body)
                .compose(neverError())
                .toObservable();
    }

    private @NonNull
    Observable<AccessTokenEnvelope> signIn(final @NonNull String loginId, final @NonNull String password) {
        final XauthBody body = new XauthBody();
        body.loginId = loginId;
        body.password = password;

        return client.signIn(body)
                .compose(pipeApiErrorsTo(signInError))
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<String> firebaseAccessToken = PublishSubject.create();
    private final PublishSubject<String> loginId = PublishSubject.create();
    private final PublishSubject<String> password = PublishSubject.create();
    private final PublishSubject<Optional> signInClick = PublishSubject.create();
    private final PublishSubject<Envelope> signInError = PublishSubject.create();

    private final BehaviorSubject<Optional> signInSuccess = BehaviorSubject.create();
    private final BehaviorSubject<FacebookException> facebookAuthorizationError = BehaviorSubject.create();
    private final BehaviorSubject<Boolean> setSignInButtonEnabled = BehaviorSubject.create();

    public final SignInViewModelInputs inputs = this;
    public final SignInViewModelOutputs outputs = this;
    public final SignInViewModelErrors errors = this;

    @Override
    public void loginId(final @NonNull String loginId) {
        this.loginId.onNext(loginId);
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
        return signInError.filter(Envelope::isDuplicatedUser)
                .map(errorEnvelope -> errorEnvelope.err.msg);
    }
}
