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
import androidx.recyclerview.widget.RecyclerView
import com.travlog.android.apps.R
import com.travlog.android.apps.models.Place
import com.travlog.android.apps.ui.viewholders.PlaceViewHolder

class PlaceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val places = ArrayList<Place>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            PlaceViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_place, parent, false))


    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            (holder as PlaceViewHolder).bindData(places[position])

    fun addData(place: Place): Boolean =
            places.add(place)
                    .apply {
                        notifyDataSetChanged()
                    }
}