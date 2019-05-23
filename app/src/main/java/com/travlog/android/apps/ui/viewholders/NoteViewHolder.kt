package com.travlog.android.apps.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.utils.DateTimeUtils
import com.travlog.android.apps.models.Note
import kotlinx.android.synthetic.main.i_note.view.*
import org.joda.time.DateTime

class NoteViewHolder(itemView: View, private val delegate: Delegate) : RecyclerView.ViewHolder(itemView) {

    interface Delegate {
        fun noteViewHolderItemClick(viewHolder: NoteViewHolder, note: Note)
    }

    fun bindData(item: Note) {
        this.itemView.note_title.text = item.title

        when {
            item.destinations.isNotEmpty() -> {
                val destination = item.destinations[0]

                this.itemView.note_destination.text = destination?.location?.name.orEmpty()
                this.itemView.note_date.text = itemView.context.getString(R.string.note_date,
                        DateTimeUtils.mediumDate(DateTime(destination?.startDate)),
                        DateTimeUtils.mediumDate(DateTime(destination?.endDate)))
            }
            else -> {
                this.itemView.note_destination.text = ""
                this.itemView.note_date.text = ""
            }
        }

        itemView.setOnClickListener { this.delegate.noteViewHolderItemClick(this, item) }
    }
}
