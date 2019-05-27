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

package com.travlog.android.apps.libs.utils

import android.app.Activity
import android.content.Context
import android.util.Pair
import com.travlog.android.apps.R

object TransitionUtils {

    fun transition(context: Context, transition: Pair<Int, Int>) {
        if (context !is Activity) {
            return
        }

        context.overridePendingTransition(transition.first, transition.second)
    }

    fun slideInFromRight(): Pair<Int, Int> {
        return Pair.create(R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }

    fun slideInFromLeft(): Pair<Int, Int> {
        return Pair.create(R.anim.fade_in_slide_in_left, R.anim.slide_out_right)
    }
}
