package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.Optional;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.ui.activities.PostNoteActivity;
import com.travlog.android.apps.viewmodels.errors.PostNoteViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.PostNoteViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.PostNoteViewModelOutputs;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen;

public class PostNoteViewModel extends ActivityViewModel<PostNoteActivity>
        implements PostNoteViewModelInputs, PostNoteViewModelOutputs, PostNoteViewModelErrors {

    public PostNoteViewModel(final @NonNull Environment environment) {
        super(environment);

        title
                .map(title -> {
                    final Note note = new Note();
                    note.title = title;
                    return note;
                })
                .compose(takeWhen(back))
                .compose(bindToLifecycle())
                .subscribe(startPostNoteService);
    }

    private final PublishSubject<String> title = PublishSubject.create();
    private final PublishSubject<Optional> back = PublishSubject.create();

    private final BehaviorSubject<Note> startPostNoteService = BehaviorSubject.create();

    public final PostNoteViewModelInputs inputs = this;
    public final PostNoteViewModelOutputs outputs = this;
    public final PostNoteViewModelErrors errors = this;

    @Override
    public void title(@NonNull String title) {
        this.title.onNext(title);
    }

    @Override
    public void back() {
        back.onNext(new Optional<>(null));
    }

    @NonNull
    @Override
    public Observable<Note> startPostNoteService() {
        return startPostNoteService;
    }
}
