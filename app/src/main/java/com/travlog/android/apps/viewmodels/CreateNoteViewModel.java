package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.ui.activities.CreateNoteActivity;
import com.travlog.android.apps.viewmodels.errors.EditNoteViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.EditNoteViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.EditNoteViewModelOutputs;

import io.reactivex.subjects.PublishSubject;

public class CreateNoteViewModel extends ActivityViewModel<CreateNoteActivity>
        implements EditNoteViewModelInputs, EditNoteViewModelOutputs, EditNoteViewModelErrors {

    public CreateNoteViewModel(final @NonNull Environment environment) {
        super(environment);
    }

    private final PublishSubject<String> title = PublishSubject.create();

    public final EditNoteViewModelInputs inputs = this;
    public final EditNoteViewModelOutputs outputs = this;
    public final EditNoteViewModelErrors errors = this;

    @Override
    public void title(@NonNull String title) {
        this.title.onNext(title);
    }
}
