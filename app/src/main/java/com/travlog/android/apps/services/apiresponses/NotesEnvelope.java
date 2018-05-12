package com.travlog.android.apps.services.apiresponses;

import com.travlog.android.apps.models.Note;

import java.util.List;

public class NotesEnvelope extends Envelope {

    public Data data;

    public class Data {
        public List<Note> notes;
    }
}
