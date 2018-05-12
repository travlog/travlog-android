package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.libs.rx.bus.NoteEvent;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.ui.activities.PostNoteActivity;
import com.travlog.android.apps.viewmodels.errors.PostNoteViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.PostNoteViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.PostNoteViewModelOutputs;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen;

public class PostNoteViewModel extends ActivityViewModel<PostNoteActivity>
        implements PostNoteViewModelInputs, PostNoteViewModelOutputs, PostNoteViewModelErrors {

    private final ApiClientType apiClient;

    public PostNoteViewModel(final @NonNull Environment environment) {
        super(environment);

        this.apiClient = environment.apiClient;

        title
                .map(title -> {
                    final Note note = new Note();
                    note.title = title;
                    return note;
                })
                .compose(takeWhen(saveClick))
                .switchMap(note -> this.post(note)
                        .doOnSubscribe(disposable -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe(this::postSuccess);
    }

    private void postSuccess(final @NonNull Note note) {
        NoteEvent.getInstance().post(note);
        back.onComplete();
    }

    private @NonNull
    Observable<Note> post(final @NonNull Note note) {
        return apiClient.postNote(note)
                .compose(neverApiError())
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<String> title = PublishSubject.create();
    private final PublishSubject<Optional> saveClick = PublishSubject.create();

    private final CompletableSubject back = CompletableSubject.create();

    public final PostNoteViewModelInputs inputs = this;
    public final PostNoteViewModelOutputs outputs = this;
    public final PostNoteViewModelErrors errors = this;

    @Override
    public void title(@NonNull String title) {
        this.title.onNext(title);
    }

    @Override
    public void saveClick() {
        this.saveClick.onNext(new Optional<>(null));
    }

    @NonNull
    @Override
    public Completable back() {
        return back;
    }
}
