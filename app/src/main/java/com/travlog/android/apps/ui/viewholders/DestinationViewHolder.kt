package com.travlog.android.apps.ui.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.travlog.android.apps.libs.utils.DateTimeUtils
import com.travlog.android.apps.models.Destination
import kotlinx.android.synthetic.main.i_destination.view.*
import org.joda.time.DateTime
import timber.log.Timber

class DestinationViewHolder(itemView: View, private val delegate: Delegate) : RecyclerView.ViewHolder(itemView) {

    interface Delegate {
        fun destinationViewHolderItemClick(viewHolder: DestinationViewHolder, destination: Destination)
    }

    fun bindData(item: Destination) {
        Timber.d("bindData? %s", item)

        if (item.startDate != null && item.endDate != null) {
            itemView.start_date.text = DateTimeUtils.mediumDate(DateTime(item.startDate))
            itemView.end_date.text = DateTimeUtils.mediumDate(DateTime(item.endDate))
        } else {
            itemView.start_date.text = ""
            itemView.end_date.text = ""
        }

        itemView.name.text = when {
            item.location.locality.isNotEmpty() -> item.location.locality
            else -> item.location.name
        }

        itemView.setOnClickListener { delegate.destinationViewHolderItemClick(this, item) }
    }
}