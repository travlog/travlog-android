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

package com.travlog.android.apps.libs.databinding.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.travlog.android.apps.libs.utils.DateTimeUtils
import com.travlog.android.apps.models.Location
import kotlinx.android.synthetic.main.item_destination.view.*
import org.joda.time.DateTime
import java.util.*

object DestinationBindingAdapter {

    @JvmStatic
    @BindingAdapter("mediumDate")
    fun mediumDate(textView: TextView, date: Date) {
        textView.text = DateTimeUtils.mediumDate(DateTime(date))
    }

    @JvmStatic
    @BindingAdapter("location")
    fun location(textView: TextView, location: Location) {
        textView.name.text = when {
            location.locality.isNotEmpty() -> location.locality
            else -> location.name
        }
    }
}