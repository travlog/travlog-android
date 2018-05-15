package com.travlog.android.apps.services.apiresponses

import com.travlog.android.apps.models.Note

data class PostNoteEnvelope(var data: Note) : Envelope()
