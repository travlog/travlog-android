package com.travlog.android.apps.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.travlog.android.apps.R
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.ui.viewholders.NoteViewHolder
import timber.log.Timber
import java.util.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.indexOf
import kotlin.collections.set

class NoteAdapter(private val delegate: Delegate) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val notes: MutableList<Note>
    private val noteMap: HashMap<String, Note>

    interface Delegate : NoteViewHolder.Delegate

    init {
        this.notes = ArrayList()
        this.noteMap = HashMap()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            NoteViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.i_note, parent, false), delegate)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            (holder as NoteViewHolder).bindData(notes[position])

    override fun getItemCount(): Int = notes.size

    fun clearData() =
            notes.clear().run {
                Timber.d("clearData")
                noteMap.clear()
                notifyDataSetChanged()
            }

    fun updateData(notes: List<Note>): Boolean {
        Timber.d("updateData? %s", notes)
        this.notes.addAll(notes).let {
            for (note in notes) {
                noteMap[note.id] = note
            }

            notifyDataSetChanged()

            return it
        }
    }

    fun updateNote(note: Note) {
        Timber.d("updateNotes? %s", note)
        val originNote = noteMap.get(note.id)

        noteMap[note.id] = note

        if (originNote != null) {
            val index = notes.indexOf(originNote)

            notes[index] = note
            notifyItemChanged(index)
        } else {
            notes.add(0, note)
            notifyItemInserted(0)
        }
    }

    fun deleteData(nid: String): Note? {
        val note = noteMap[nid]
        val index = notes.indexOf(note)

        notes.removeAt(index)
        noteMap.remove(nid)
        notifyItemRemoved(index)

        return note
    }
}
