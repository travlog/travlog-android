/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
