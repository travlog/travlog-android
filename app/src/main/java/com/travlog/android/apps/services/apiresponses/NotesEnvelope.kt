package com.travlog.android.apps.services.apiresponses

import com.travlog.android.apps.models.Note

data class NotesEnvelope(var data: Data) : Envelope() {

    data class Data(var list: List<Note>)
}
