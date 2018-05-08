package com.travlog.android.apps.services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.travlog.android.apps.TravlogApplication;
import com.travlog.android.apps.libs.CurrentUserType;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.models.Note;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;
import static com.travlog.android.apps.ui.IntentKey.NOTE;

public class PostNoteService extends JobIntentService {

    private ApiClientType apiClient;
    private CurrentUserType currentUser;
    private CompositeDisposable disposables;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.d("onCreate: %s", this.toString());

        final TravlogApplication application = (TravlogApplication) getApplication();
        final Environment environment = application.component().environment();

        this.apiClient = environment.apiClient;
        this.currentUser = environment.currentUser;

        this.disposables = new CompositeDisposable();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Timber.d("onHandleWork: %s", this.toString());
        final Note note = intent.getParcelableExtra(NOTE);

        Timber.d("onHandleWork: note? %s", note);

        disposables.add(
                currentUser.loggedInUser()
                        .map(user -> user.userId)
                        .switchMap(userId -> this.post(userId, note))
                        .subscribe());
    }

    private @NonNull
    Observable<Note> post(final @NonNull String userId, final @NonNull Note note) {
        return apiClient.postNote(userId, note)
                .compose(neverError())
                .toObservable();
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy: %s", this.toString());

        disposables.clear();

        super.onDestroy();
    }
}
