package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

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
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.models.Account;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.services.apirequests.OauthBody;
import com.travlog.android.apps.services.apiresponses.ProfileEnvelope;
import com.travlog.android.apps.ui.activities.LinkedAccountsActivity;
import com.travlog.android.apps.viewmodels.errors.LinkedAccountsViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.LinkedAccountsViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.LinkedAccountsViewModelOutputs;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;

public class LinkedAccountsViewModel extends ActivityViewModel<LinkedAccountsActivity>
        implements LinkedAccountsViewModelInputs, LinkedAccountsViewModelOutputs, LinkedAccountsViewModelErrors {

    private final ApiClientType apiClient;
    private final CallbackManager callbackManager;

    public LinkedAccountsViewModel(final @NonNull Environment environment) {
        super(environment);

        this.apiClient = environment.apiClient;
        this.callbackManager = CallbackManager.Factory.create();

        activityResult()
                .filter(activityResult -> activityResult.requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode())
                .compose(bindToLifecycle())
                .subscribe(activityResult -> callbackManager.onActivityResult(activityResult.requestCode, activityResult.resultCode, activityResult.intent));

        activityResult()
                .filter(activityResult -> activityResult.requestCode == SIGN_IN_WITH_GOOGLE)
                .map(activityResult -> activityResult.intent)
                .map(GoogleSignIn::getSignedInAccountFromIntent)
                .map(Task::getResult)
                .map(GoogleSignInAccount::getIdToken)
                .switchMap(token -> this.link(environment.currentUser.getUser().userId, token, "google"))
                .compose(bindToLifecycle())
                .subscribe();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();
                facebookAccessToken.onNext(accessToken.getToken());
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

        facebookAccessToken
                .switchMap(token -> this.link(environment.currentUser.getUser().userId, token, "facebook"))
                .compose(bindToLifecycle())
                .subscribe();
    }

    private @NonNull
    Observable<List<Account>> link(final @NonNull String userId, final @NonNull String token, final @NonNull String provider) {
        final OauthBody body = new OauthBody();
        body.token = token;
        body.provider = provider;

        return apiClient.linkAccounts(userId, body)
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<String> facebookAccessToken = PublishSubject.create();

    private final BehaviorSubject<Optional> signInSuccess = BehaviorSubject.create();
    private final BehaviorSubject<FacebookException> facebookAuthorizationError = BehaviorSubject.create();

    public final LinkedAccountsViewModelInputs inputs = this;
    public final LinkedAccountsViewModelOutputs outputs = this;
    public final LinkedAccountsViewModelErrors errors = this;
}
