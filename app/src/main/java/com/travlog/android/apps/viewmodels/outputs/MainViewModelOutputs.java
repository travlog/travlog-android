package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;

import java.util.List;

import io.reactivex.Observable;

public interface MainViewModelOutputs {

    @NonNull
    Observable<List<Note>> updateData();

    @NonNull
    Observable<Note> showNoteActivity();

    @NonNull
    Observable<Integer> deleteNote();
}
