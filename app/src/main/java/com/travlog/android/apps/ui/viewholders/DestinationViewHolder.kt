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

package com.travlog.android.apps.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.travlog.android.apps.databinding.ItemDestinationBinding
import com.travlog.android.apps.models.Destination
import timber.log.Timber

class DestinationViewHolder(private val binding: ItemDestinationBinding,
                            private val delegate: Delegate
) : RecyclerView.ViewHolder(binding.root) {

    interface Delegate {
        fun destinationViewHolderItemClick(viewHolder: DestinationViewHolder, destination: Destination)
    }

    fun bindData(item: Destination, last: Boolean) {
        Timber.d("bindData? %s", item)

        binding.destination = item

        itemView.setOnClickListener { delegate.destinationViewHolderItemClick(this, item) }
    }
}