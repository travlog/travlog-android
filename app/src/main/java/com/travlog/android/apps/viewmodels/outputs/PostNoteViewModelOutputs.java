package com.travlog.android.apps.viewmodels.outputs;

import android.support.annotation.NonNull;

import io.reactivex.Completable;

public interface PostNoteViewModelOutputs {

    @NonNull
    Completable back();
}
