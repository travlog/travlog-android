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

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.travlog.android.apps.libs.utils.DateTimeUtils
import com.travlog.android.apps.models.Destination
import kotlinx.android.synthetic.main.i_destination.view.*
import org.joda.time.DateTime
import timber.log.Timber

class DestinationViewHolder(itemView: View, private val delegate: Delegate) : RecyclerView.ViewHolder(itemView) {

    interface Delegate {
        fun destinationViewHolderItemClick(viewHolder: DestinationViewHolder, destination: Destination)
    }

    fun bindData(item: Destination, last: Boolean) {
        Timber.d("bindData? %s", item)

        if (item.startDate != null && item.endDate != null) {
            itemView.start_date.text = DateTimeUtils.mediumDate(DateTime(item.startDate))
            itemView.end_date.text = when {
                last -> DateTimeUtils.mediumDate(DateTime(item.endDate))
                else -> ""
            }
        } else {
            itemView.start_date.text = ""
            itemView.end_date.text = ""
        }

        itemView.name.text = when {
            item.location == null -> ""
            item.location!!.locality.isNotEmpty() -> item.location!!.locality
            else -> item.location!!.name
        }

        itemView.setOnClickListener { delegate.destinationViewHolderItemClick(this, item) }
    }
}