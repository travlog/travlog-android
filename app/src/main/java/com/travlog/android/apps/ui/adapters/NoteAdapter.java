package com.travlog.android.apps.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.travlog.android.apps.R;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.ui.viewholders.NoteViewHolder;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Delegate extends NoteViewHolder.Delegate {
    }

    private final Delegate delegate;
    private final List<Note> notes;
    private final SparseArray<Note> noteSparseArray;

    public NoteAdapter(final @NonNull Delegate delegate) {
        this.delegate = delegate;
        this.notes = new ArrayList<>();
        this.noteSparseArray = new SparseArray<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_note, parent, false), delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NoteViewHolder) holder).bindData(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public boolean updateData(final @NonNull List<Note> notes) {
        final boolean result = this.notes.addAll(notes);

        for (Note note : notes) {
            noteSparseArray.put(note.id, note);
        }

        notifyDataSetChanged();
        return result;
    }

    public @NonNull
    Note deleteData(final int noteId) {
        final Note note = noteSparseArray.get(noteId);
        final int index = notes.indexOf(note);

        notes.remove(index);
        noteSparseArray.delete(noteId);
        notifyItemRemoved(index);

        return note;
    }
}
