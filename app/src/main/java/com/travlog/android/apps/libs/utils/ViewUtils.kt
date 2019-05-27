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

import android.view.View
import java.util.function.Consumer

object ViewUtils {

    /**
     * Sets the visiblity of a view to {@link View#VISIBLE} or {@link View#GONE}. Setting
     * the view to GONE removes it from the layout so that it no longer takes up any space.
     */
    fun setGone(view: View, gone: Boolean) {
        if (gone) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    fun setGone(view: View): Consumer<Boolean> {
        return Consumer { gone -> setGone(view, gone) }
    }
}
