package com.travlog.android.apps.viewmodels;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.services.apirequests.XauthBody;
import com.travlog.android.apps.services.apiresponses.AccessTokenEnvelope;
import com.travlog.android.apps.services.apiresponses.Envelope;
import com.travlog.android.apps.ui.activities.SignUpActivity;
import com.travlog.android.apps.viewmodels.errors.SignUpViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.SignUpViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SignUpViewModelOutputs;

import java.util.regex.Pattern;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.combineLatestPair;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.pipeApiErrorsTo;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen;

public class SignUpViewModel extends ActivityViewModel<SignUpActivity>
        implements SignUpViewModelInputs, SignUpViewModelOutputs, SignUpViewModelErrors {

    private final ApiClientType apiClient;

    @SuppressLint("CheckResult")
    public SignUpViewModel(final @NonNull Environment environment) {
        super(environment);

        this.apiClient = environment.apiClient;

        final Observable<Boolean> isValidEmail = this.email.map(this::isValidEmail);

        final Observable<Boolean> isValidPassword = this.password.map(this::isValidPassword);

        Observable.merge(
                isValidEmail,
                isValidPassword
        )
                .compose(bindToLifecycle())
                .subscribe(setNextButtonEnabled);


        email.compose(combineLatestPair(password))
                .compose(takeWhen(signUpClick))
                .compose(bindToLifecycle())
                .switchMap(ep -> this.signUp(ep.first, ep.second))
                .compose(bindToLifecycle())
                .subscribe(envelope -> {
                    environment.currentUser.login(envelope.data.user, envelope.data.accessToken);
                    signUpSuccess.onComplete();
                });

        duplicatedError()
                .compose(bindToLifecycle())
                .subscribe(msg -> Timber.d("SignUpViewModel: duplicatedError: %s", msg));
    }

    private boolean isValidEmail(final @NonNull String email) {
        return Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email);
    }

    private boolean isValidPassword(final @NonNull String password) {
        return !TextUtils.isEmpty(password);
    }

    private @NonNull
    Observable<AccessTokenEnvelope> signUp(final @NonNull String email,
                                           final @NonNull String password) {

        final XauthBody body = new XauthBody();
        body.email = email;
        body.password = password;

        return apiClient.signup(body)
                .compose(pipeApiErrorsTo(signUpError))
//                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<String> email = PublishSubject.create();
    private final PublishSubject<String> password = PublishSubject.create();
    private final PublishSubject<Optional> signUpClick = PublishSubject.create();
    private final PublishSubject<Envelope> signUpError = PublishSubject.create();

    private final BehaviorSubject<Boolean> setNextButtonEnabled = BehaviorSubject.create();
    private final CompletableSubject signUpSuccess = CompletableSubject.create();

    public final SignUpViewModelInputs inputs = this;
    public final SignUpViewModelOutputs outputs = this;
    public final SignUpViewModelErrors errors = this;

    @Override
    public void email(final @NonNull String email) {
        this.email.onNext(email);
    }

    @Override
    public void password(final @NonNull String password) {
        this.password.onNext(password);
    }

    @Override
    public void signUpClick() {
        this.signUpClick.onNext(new Optional<>(null));
    }

    @NonNull
    @Override
    public Observable<Boolean> setNextButtonEnabled() {
        return setNextButtonEnabled;
    }

    @NonNull
    @Override
    public Completable signUpSuccess() {
        return signUpSuccess;
    }

    @NonNull
    @Override
    public Observable<String> duplicatedError() {
        return signUpError.filter(Envelope::isDuplicatedUser)
                .map(errorEnvelope -> errorEnvelope.err.msg);
    }
}
