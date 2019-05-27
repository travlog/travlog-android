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

package com.travlog.android.apps.libs.rx.views

import com.arlib.floatingsearchview.FloatingSearchView
import com.jakewharton.rxbinding3.InitialValueObservable
import com.travlog.android.apps.libs.rx.operators.QueryObservable

object RxFloatingSearchView {

    fun queryChanges(
            view: FloatingSearchView, characterLimit: Int = 1): InitialValueObservable<CharSequence> {
        checkNotNull(view, "view == null")
        return QueryObservable(view, characterLimit)
    }

    fun checkNotNull(value: Any?, message: String) {
        if (value == null) {
            throw NullPointerException(message)
        }
    }
}
