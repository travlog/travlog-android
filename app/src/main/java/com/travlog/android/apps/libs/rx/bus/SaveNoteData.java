package com.travlog.android.apps.libs.rx.bus;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;

import rx.Observable;
import rx.subjects.PublishSubject;

public final class SaveNoteData {

    private static SaveNoteData instance;
    private final PublishSubject<Note> subject;

    private SaveNoteData() {
        subject = PublishSubject.create();
    }

    public static SaveNoteData getInstance() {
        if (instance == null) {
            instance = new SaveNoteData();
        }
        return instance;
    }

    public void post(final @NonNull Note note) {
        subject.onNext(note);
    }

    public @NonNull
    Observable<Note> asObservable() {
        return subject;
    }
}
