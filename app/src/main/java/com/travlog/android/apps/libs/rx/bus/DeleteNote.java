package com.travlog.android.apps.libs.rx.bus;

import android.support.annotation.NonNull;

import com.travlog.android.apps.models.Note;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class DeleteNote {

    private static DeleteNote instance;

    private final PublishSubject<Note> subject;

    private DeleteNote() {
        subject = PublishSubject.create();
    }

    public static @NonNull
    DeleteNote getInstance() {
        if (instance == null) {
            instance = new DeleteNote();
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
