package com.travlog.android.apps.libs;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.travlog.android.apps.libs.db.realm.RealmHelper;
import com.travlog.android.apps.libs.perferences.StringPreferenceType;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.libs.utils.ObjectUtils;
import com.travlog.android.apps.models.User;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public class CurrentUser extends CurrentUserType {

    private final StringPreferenceType accessTokenPreference;
    private final StringPreferenceType userPreference;

    private final BehaviorSubject<Optional<User>> user = BehaviorSubject.create();

    @SuppressLint("CheckResult")
    public CurrentUser(final @NonNull StringPreferenceType accessTokenPreference,
                       final @NonNull StringPreferenceType userPreference) {
        this.accessTokenPreference = accessTokenPreference;
        this.userPreference = userPreference;

        final Gson gson = new Gson();

        this.user
                .skip(1)
                .filter(Optional::isNotEmpty)
                .map(Optional::get)
                .subscribe(u -> userPreference.set(gson.toJson(u, User.class)));

        this.user.onNext(new Optional<>(gson.fromJson(userPreference.get(), User.class)));
    }

    @Override
    public @NonNull
    Optional<User> getUser() {
        return this.user.getValue();
    }

    @Override
    public boolean exists() {
        return getUser().isNotEmpty();
    }

    public String getAccessToken() {
        return this.accessTokenPreference.get();
    }

    @Override
    public void login(final @NonNull User newUser, final @NonNull String accessToken) {
        Timber.d("Login user %s", newUser.name);

        this.accessTokenPreference.set(accessToken);
        this.user.onNext(new Optional<>(newUser));
    }

    @Override
    public void logout() {
        Timber.d("Logout current user");

        this.userPreference.delete();
        this.accessTokenPreference.delete();
        this.user.onNext(new Optional<>(null));
        LoginManager.getInstance().logOut();
        RealmHelper.getInstance().deleteAllAsync();
    }

    @Override
    public void refresh(final @NonNull User freshUser) {
        this.user.onNext(new Optional<>(freshUser));
    }

    @Override
    public @NonNull
    Observable<Optional<User>> observable() {
        return this.user;
    }
}
