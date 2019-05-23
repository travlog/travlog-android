package com.travlog.android.apps.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travlog.android.apps.R
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.ui.viewholders.DestinationViewHolder

class DestinationAdapter(private val delegate: Delegate) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val destinations: MutableList<Destination>

    interface Delegate : DestinationViewHolder.Delegate

    init {
        this.destinations = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DestinationViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.i_destination, parent, false), delegate)

    override fun getItemCount(): Int = destinations.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            (holder as DestinationViewHolder).bindData(destinations[position], position == destinations.size - 1)

    fun updateData(destinations: List<Destination>): Boolean =
            this.destinations.addAll(destinations).let {
                notifyDataSetChanged()
                return it
            }

    fun addData(destination: Destination): Boolean =
            destinations.add(destination).let {
                notifyItemInserted(destinations.size - 1)
                return it
            }

    fun clearData() {
        destinations.clear()
        notifyDataSetChanged()
    }
}
