/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travlog.android.apps.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.travlog.android.apps.R
import com.travlog.android.apps.databinding.ItemDestinationBinding
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.ui.viewholders.DestinationViewHolder

class DestinationAdapter(private val delegate: Delegate) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val destinations: MutableList<Destination>

    interface Delegate : DestinationViewHolder.Delegate

    init {
        this.destinations = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context)
                    .let { inflater ->
                        DataBindingUtil.inflate<ItemDestinationBinding>(inflater, R.layout.item_destination, parent, false)
                                .let { binding ->
                                    DestinationViewHolder(binding, delegate)
                                }
                    }

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
