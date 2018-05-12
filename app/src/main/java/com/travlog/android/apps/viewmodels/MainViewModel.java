package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.rx.bus.DeleteNote;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.ui.activities.MainActivity;
import com.travlog.android.apps.ui.adapters.NoteAdapter;
import com.travlog.android.apps.ui.viewholders.NoteViewHolder;
import com.travlog.android.apps.viewmodels.errors.MainViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.MainViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.MainViewModelOutputs;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError;
import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;

public class MainViewModel extends ActivityViewModel<MainActivity>
        implements MainViewModelInputs, MainViewModelOutputs, MainViewModelErrors, NoteAdapter.Delegate {

    private final ApiClientType apiClient;

    public MainViewModel(final @NonNull Environment environment) {
        super(environment);

        this.apiClient = environment.apiClient;

        this.showNoteActivity = this.noteItemClick;

        notes()
                .compose(bindToLifecycle())
                .subscribe(updateData::onNext);

        deleteNote = DeleteNote.getInstance().getObservable()
                .map(note -> note.id);
    }

    private @NonNull
    Observable<List<Note>> notes() {
        return apiClient.notes()
                .compose(neverApiError())
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<Note> noteItemClick = PublishSubject.create();

    private final BehaviorSubject<List<Note>> updateData = BehaviorSubject.create();
    private Observable<Note> showNoteActivity;
    private Observable<Integer> deleteNote;

    public final MainViewModelInputs inputs = this;
    public final MainViewModelOutputs outputs = this;
    public final MainViewModelErrors errors = this;

    @NonNull
    @Override
    public Observable<List<Note>> updateData() {
        return updateData;
    }

    @NonNull
    @Override
    public Observable<Note> showNoteActivity() {
        return showNoteActivity;
    }

    @NonNull
    @Override
    public Observable<Integer> deleteNote() {
        return deleteNote;
    }

    @Override
    public void noteViewHolderItemClick(final @NonNull NoteViewHolder viewHolder,
                                        final @NonNull Note note) {

        noteItemClick.onNext(note);
    }
}
