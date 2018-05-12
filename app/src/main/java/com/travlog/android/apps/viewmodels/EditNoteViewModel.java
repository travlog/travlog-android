package com.travlog.android.apps.viewmodels;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.libs.rx.bus.NoteEvent;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.ui.activities.EditNoteActivity;
import com.travlog.android.apps.viewmodels.errors.EditNoteViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.EditNoteViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.EditNoteViewModelOutputs;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;
import static com.travlog.android.apps.ui.IntentKey.NOTE;

public class EditNoteViewModel extends ActivityViewModel<EditNoteActivity>
        implements EditNoteViewModelInputs, EditNoteViewModelOutputs, EditNoteViewModelErrors {

    private final ApiClientType apiClient;

    private Note note;

    @SuppressLint("CheckResult")
    public EditNoteViewModel(final @NonNull Environment environment) {
        super(environment);

        this.apiClient = environment.apiClient;

        intent()
                .filter(i -> i.getParcelableExtra(NOTE) != null)
                .map(i -> (Note) i.getParcelableExtra(NOTE))
                .doOnNext(note -> this.note = note)
                .subscribe(note -> {
                    setTitleText.onNext(note.title);
                });

        title
                .compose(bindToLifecycle())
                .subscribe(title -> note.title = title);

        saveClick
                .switchMap(__ -> this.update(this.note)
                        .doOnSubscribe(disposable -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .subscribe(this::updateSuccess);
    }

    private void updateSuccess(final @NonNull Note note) {
        NoteEvent.getInstance().post(note);
        back.onComplete();
    }

    private @NonNull
    Observable<Note> update(final @NonNull Note note) {
        return apiClient.updateNote(note)
                .compose(neverApiError())
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<String> title = PublishSubject.create();
    private final PublishSubject<Optional> saveClick = PublishSubject.create();

    private final BehaviorSubject<String> setTitleText = BehaviorSubject.create();
    private final CompletableSubject back = CompletableSubject.create();

    public final EditNoteViewModelInputs inputs = this;
    public final EditNoteViewModelOutputs outputs = this;
    public final EditNoteViewModelErrors errors = this;

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
    public Observable<String> setTitleText() {
        return setTitleText;
    }

    @NonNull
    @Override
    public Completable back() {
        return back;
    }
}
