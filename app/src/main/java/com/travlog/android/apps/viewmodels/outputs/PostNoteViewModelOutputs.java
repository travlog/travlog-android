package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;

import io.reactivex.Observable;

public interface PostNoteViewModelOutputs {

    @NonNull
    Observable<Note> startPostNoteService();
}
