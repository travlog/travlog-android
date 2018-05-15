package com.travlog.android.apps.ui.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.travlog.android.apps.models.Note
import kotlinx.android.synthetic.main.i_note.view.*

class NoteViewHolder(itemView: View, private val delegate: Delegate) : RecyclerView.ViewHolder(itemView) {

    interface Delegate {
        fun noteViewHolderItemClick(viewHolder: NoteViewHolder, note: Note)
    }

    fun bindData(item: Note) {
        this.itemView.note_title.text = item.title

        itemView.setOnClickListener { view -> this.delegate.noteViewHolderItemClick(this, item) }
    }
}
