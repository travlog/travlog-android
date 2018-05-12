package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface NoteViewModelOutputs {

    @NonNull
    Observable<String> setTitleText();

    @NonNull
    Observable<Note> showEditNoteActivity();

    @NonNull
    Completable back();
}