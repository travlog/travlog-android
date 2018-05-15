package com.travlog.android.apps.services.apiresponses

import com.travlog.android.apps.models.Note

data class NoteEnvelope(var data: Note) : Envelope()
