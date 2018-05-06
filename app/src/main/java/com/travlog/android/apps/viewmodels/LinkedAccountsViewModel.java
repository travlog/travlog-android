package com.travlog.android.apps.viewmodels;

import android.net.Uri;
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
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.ProfileEnvelope;
import com.travlog.android.apps.ui.activities.LinkedAccountsActivity;
import com.travlog.android.apps.viewmodels.errors.LinkedAccountsViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.LinkedAccountsViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.LinkedAccountsViewModelOutputs;

import org.json.JSONException;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.travlog.android.apps.libs.ActivityRequestCodes.SIGN_IN_WITH_GOOGLE;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.pipeApiErrorsTo;

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
                .map(googleSignInAccount -> {
                    final XauthBody body = new XauthBody();
                    body.userId = googleSignInAccount.getId();
                    body.name = googleSignInAccount.getDisplayName();
                    body.email = googleSignInAccount.getEmail();

                    final Uri photoUrl = googleSignInAccount.getPhotoUrl();
                    if (photoUrl != null) {
                        body.profilePicture = photoUrl.toString();
                    }
                    body.type = "google";

                    return body;
                })
                .compose(bindToLifecycle())
                .subscribe(xauthBody);

        xauthBody
                .switchMap(body -> this.link(environment.currentUser.getUser().get().userId, body)
                        .doOnSubscribe(subscription -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe();



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
                        body.profilePicture = obj.getJSONObject("picture").getJSONObject("data").getString("url");
                        body.type = "facebook";

                        xauthBody.onNext(body);
                    } catch (JSONException e) {
                        Timber.w(e, "onSuccess: ");
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email,picture");
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
    }

    private @NonNull
    Observable<ProfileEnvelope> link(final @NonNull String userId, final @NonNull XauthBody body) {
        return apiClient.linkAccounts(userId, body)
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<XauthBody> xauthBody = PublishSubject.create();

    private final BehaviorSubject<Optional> signInSuccess = BehaviorSubject.create();
    private final BehaviorSubject<FacebookException> facebookAuthorizationError = BehaviorSubject.create();

    public final LinkedAccountsViewModelInputs inputs = this;
    public final LinkedAccountsViewModelOutputs outputs = this;
    public final LinkedAccountsViewModelErrors errors = this;
}
