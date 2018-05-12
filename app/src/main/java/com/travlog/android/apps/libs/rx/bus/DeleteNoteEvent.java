package com.travlog.android.apps.libs.rx.bus;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class DeleteNoteEvent {

    private static DeleteNoteEvent instance;

    private final PublishSubject<Note> subject;

    private DeleteNoteEvent() {
        subject = PublishSubject.create();
    }

    public static @NonNull
    DeleteNoteEvent getInstance() {
        if (instance == null) {
            instance = new DeleteNoteEvent();
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
