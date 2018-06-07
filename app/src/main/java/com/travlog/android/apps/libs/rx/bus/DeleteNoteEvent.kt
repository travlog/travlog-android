package com.travlog.android.apps.libs.rx.bus

import com.travlog.android.apps.models.Note

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class DeleteNoteEvent private constructor() {

    private val subject: PublishSubject<String> = PublishSubject.create()

    val observable: Observable<String>
        get() = subject

    fun post(noteId: String) {
        this.subject.onNext(noteId)
    }

    companion object {

        private var instance: DeleteNoteEvent? = null

        fun getInstance(): DeleteNoteEvent {
            if (instance == null) {
                instance = DeleteNoteEvent()
            }
            return instance as DeleteNoteEvent
        }
    }
}
