package com.travlog.android.apps.ui.adapters

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup

import com.travlog.android.apps.R
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.ui.viewholders.NoteViewHolder

import java.util.ArrayList

class NoteAdapter(private val delegate: Delegate) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val notes: MutableList<Note>
    private val noteSparseArray: SparseArray<Note>

    interface Delegate : NoteViewHolder.Delegate

    init {
        this.notes = ArrayList()
        this.noteSparseArray = SparseArray()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.i_note, parent, false), delegate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NoteViewHolder).bindData(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateData(notes: List<Note>): Boolean {
        val result = this.notes.addAll(notes)

        for (note in notes) {
            noteSparseArray.put(note.id, note)
        }

        notifyDataSetChanged()
        return result
    }

    fun updateData(note: Note) {
        val originNote = noteSparseArray.get(note.id)


        noteSparseArray.put(note.id, note)

        if (originNote != null) {
            val index = notes.indexOf(originNote)

            notes[index] = note
            notifyItemChanged(index)
        } else {
            notes.add(0, note)
            notifyItemInserted(0)
        }
    }

    fun deleteData(noteId: Int): Note {
        val note = noteSparseArray.get(noteId)
        val index = notes.indexOf(note)

        notes.removeAt(index)
        noteSparseArray.delete(noteId)
        notifyItemRemoved(index)

        return note
    }
}
