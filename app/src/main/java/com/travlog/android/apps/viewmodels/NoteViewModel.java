package com.travlog.android.apps.viewmodels;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.libs.rx.bus.DeleteNote;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.ui.activities.NoteActivity;
import com.travlog.android.apps.viewmodels.errors.NoteViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.NoteViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.NoteViewModelOutputs;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen;
import static com.travlog.android.apps.ui.IntentKey.NOTE;

public class NoteViewModel extends ActivityViewModel<NoteActivity> implements
        NoteViewModelInputs, NoteViewModelOutputs, NoteViewModelErrors {

    private final ApiClientType apiClient;

    @SuppressLint("CheckResult")
    public NoteViewModel(final @NonNull Environment environment) {
        super(environment);

        this.apiClient = environment.apiClient;

        final Observable<Note> noteIntent = intent()
                .filter(i -> i.getParcelableExtra(NOTE) != null)
                .map(i -> (Note) i.getParcelableExtra(NOTE));

        noteIntent
                .compose(bindToLifecycle())
                .subscribe(note -> {
                    setTitleText.onNext(note.title);
                });

        noteIntent
                .compose(takeWhen(deleteClick))
                .switchMap(note -> this.delete(note.id)
                        .doOnSubscribe(disposable -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe(this::deleteSuccess);
    }

    private void deleteSuccess(final @NonNull Note note) {
        // TODO: 2018. 5. 12. note not found error 발생 시에도 delete 성공과 똑같이 처리한다
        DeleteNote.getInstance().post(note);
        back.onComplete();
    }

    private @NonNull
    Observable<Note> delete(final long noteId) {
        return apiClient.deleteNote(noteId)
                .compose(neverApiError())
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<Optional> deleteClick = PublishSubject.create();

    private final BehaviorSubject<String> setTitleText = BehaviorSubject.create();
    private final CompletableSubject back = CompletableSubject.create();

    public final NoteViewModelInputs inputs = this;
    public final NoteViewModelOutputs outputs = this;
    public final NoteViewModelErrors errors = this;

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

    @Override
    public void deleteClick() {
        this.deleteClick.onNext(new Optional<>(null));
    }
}
