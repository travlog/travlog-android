package com.travlog.android.apps.libs.rx.bus

import com.travlog.android.apps.models.Note

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class NoteEvent private constructor() {

    private val subject: PublishSubject<Note> = PublishSubject.create()

    val observable: Observable<Note>
        get() = subject

    fun post(note: Note) {
        this.subject.onNext(note)
    }

    companion object {

        private var instance: NoteEvent? = null

        fun getInstance(): NoteEvent {
            if (instance == null) {
                instance = NoteEvent()
            }
            return instance as NoteEvent
        }
    }
}
