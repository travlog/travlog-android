package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;

import java.util.List;

import io.reactivex.Observable;

public interface MainViewModelOutputs {

    @NonNull
    Observable<List<Note>> updateData();

    @NonNull
    Observable<Note> showNoteDetailsActivity();

    @NonNull
    Observable<Note> updateNote();

    @NonNull
    Observable<Integer> deleteNote();
}
