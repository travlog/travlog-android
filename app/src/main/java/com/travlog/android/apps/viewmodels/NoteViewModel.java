package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.ui.activities.NoteActivity;
import com.travlog.android.apps.viewmodels.errors.NoteViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.NoteViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.NoteViewModelOutputs;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.travlog.android.apps.ui.IntentKey.NOTE;

public class NoteViewModel extends ActivityViewModel<NoteActivity> implements
        NoteViewModelInputs, NoteViewModelOutputs, NoteViewModelErrors {

    public NoteViewModel(final @NonNull Environment environment) {
        super(environment);

        intent()
                .filter(i -> i.getParcelableExtra(NOTE) != null)
                .map(i -> (Note) i.getParcelableExtra(NOTE))
                .compose(bindToLifecycle())
                .subscribe(note -> {
                    setTitleText.onNext(note.title);
                });
    }

    private final BehaviorSubject<String> setTitleText = BehaviorSubject.create();

    public final NoteViewModelInputs inputs = this;
    public final NoteViewModelOutputs outputs = this;
    public final NoteViewModelErrors errors = this;

    @NonNull
    @Override
    public Observable<String> setTitleText() {
        return setTitleText;
    }
}
