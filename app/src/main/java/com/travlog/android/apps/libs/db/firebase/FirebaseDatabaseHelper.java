package com.travlog.android.apps.libs.db.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.travlog.android.apps.models.Note;

public class FirebaseDatabaseHelper {

    private static FirebaseDatabaseHelper instance;
    private final DatabaseReference notesRef;

    private FirebaseDatabaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        notesRef = database.getReference("notes");
    }

    public static FirebaseDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDatabaseHelper();
        }
        return instance;
    }

    public void createNote(final @NonNull Note note) {
//        final DatabaseReference ref = notesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//        note.nid = ref.push().getKey();
//
//        ref.push()
//                .setValue(note)
//                .addOnCompleteListener(runnable -> {
//                    if (runnable.isSuccessful()) {
//                        note.synced = true;
//                        RealmHelper.getInstance().setNoteAsync(note);
//                        Timber.d("setNoteAsync: success");
//                    } else {
//                        Timber.e(runnable.getException(), "setNoteAsync: failed");
//                    }
//                });
    }

    public void setNoteSynced(final @NonNull String nid, final boolean synced) {
//        final DatabaseReference ref = notesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(nid);
    }

    public void updateNote(final @NonNull Note note) {
        // TODO: 2017. 12. 20. modify and save into realm
    }
}
