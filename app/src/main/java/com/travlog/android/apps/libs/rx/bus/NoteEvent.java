package com.travlog.android.apps.libs.rx.bus;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class NoteEvent {

    private final PublishSubject<Note> subject;

    private static NoteEvent instance;

    private NoteEvent() {
        subject = PublishSubject.create();
    }

    public static @NonNull
    NoteEvent getInstance() {
        if (instance == null) {
            instance = new NoteEvent();
        }
        return instance;
    }

    public void post(final @NonNull Note note) {
        this.subject.onNext(note);
    }

    public @NonNull
    Observable<Note> getObservable() {
        return subject;
    }
}
