package com.travlog.android.apps.viewmodels;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.CurrentUserType;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.models.User;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.services.apiresponses.Envelope;
import com.travlog.android.apps.ui.activities.SetUsernameActivity;
import com.travlog.android.apps.viewmodels.errors.SetUsernameViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.SetUsernameViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SetUsernameViewModelOutputs;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.pipeApiErrorsTo;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen;

public class SetUsernameViewModel extends ActivityViewModel<SetUsernameActivity>
        implements SetUsernameViewModelInputs, SetUsernameViewModelOutputs, SetUsernameViewModelErrors {

    private final CurrentUserType currentUser;
    private final ApiClientType apiClient;

    @SuppressLint("CheckResult")
    public SetUsernameViewModel(final @NonNull Environment environment) {
        super(environment);

        this.currentUser = environment.currentUser;
        this.apiClient = environment.apiClient;

        final Observable<Boolean> isValid = username.map(this::isValid);

        isValid
                .compose(bindToLifecycle())
                .subscribe(setSaveButtonEnabled);

        username
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(username -> this.userByUsername(username)
                        .doOnSubscribe(disposable -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe();

        username
                .compose(takeWhen(saveClick))
                .switchMap(username -> this.setUsername(currentUser.getUser().get().userId, username)
                        .doOnSubscribe(disposable -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe(this::setUsernameSuccess);
    }

    private void setUsernameSuccess(final @NonNull User user) {
        currentUser.refresh(user);
        back.onComplete();
    }

    private boolean isValid(final @NonNull String username) {
        return !TextUtils.isEmpty(username);
    }

    private @NonNull
    Observable<User> userByUsername(final @NonNull String username) {
        return apiClient.userByUsername(username)
                .compose(pipeApiErrorsTo(profileError))
                .compose(neverError())
                .toObservable();
    }

    private @NonNull
    Observable<User> setUsername(final @NonNull String userId, final @NonNull String username) {
        final User user = new User();
        user.username = username;

        return apiClient.updateProfile(userId, user)
                .compose(pipeApiErrorsTo(profileError))
                .compose(neverError())
                .toObservable();
    }

    public final SetUsernameViewModelInputs inputs = this;
    public final SetUsernameViewModelOutputs outputs = this;
    public final SetUsernameViewModelErrors errors = this;

    private final PublishSubject<String> username = PublishSubject.create();
    private final PublishSubject<Optional> saveClick = PublishSubject.create();
    private final PublishSubject<Envelope> profileError = PublishSubject.create();

    private final BehaviorSubject<Boolean> setSaveButtonEnabled = BehaviorSubject.create();
    private final CompletableSubject back = CompletableSubject.create();

    @Override
    public void username(@NonNull String username) {
        this.username.onNext(username);
    }

    @Override
    public void saveClick() {
        this.saveClick.onNext(new Optional<>(null));
    }

    @NonNull
    @Override
    public Observable<Boolean> setSaveButtonEnabled() {
        return setSaveButtonEnabled;
    }

    @NonNull
    @Override
    public Completable back() {
        return back;
    }
}
