package com.travlog.android.apps.viewmodels.outputs;

import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.models.User;

import io.realm.RealmResults;
import rx.Observable;

public interface NoteViewModelOutputs {

    Observable<Integer> startSyncNoteService();

    Observable<User> startSyncNotesService();

    Observable<RealmResults<Note>> updateData();

    Observable<Integer> startEditNoteActivity();

    Observable<Integer> showDeleteNoteConfirmDialog();

    Observable<Integer> showNoteDeletedSnackbar();
}
