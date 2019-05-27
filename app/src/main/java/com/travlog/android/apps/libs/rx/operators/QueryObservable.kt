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

package com.travlog.android.apps.libs.rx.operators

import com.arlib.floatingsearchview.FloatingSearchView
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class QueryObservable @JvmOverloads constructor(private val view: FloatingSearchView, private val minQueryLength: Int = 1) : InitialValueObservable<CharSequence>() {

    override val initialValue: CharSequence
        get() = view.query

    override fun subscribeListener(observer: Observer<in CharSequence>) {
        val listener = Listener(view, observer, minQueryLength)
        observer.onSubscribe(listener)
        view.setOnQueryChangeListener(listener)
    }

    internal class Listener(private val view: FloatingSearchView, private val observer: Observer<in CharSequence>, private val minQueryLength: Int) : MainThreadDisposable(), FloatingSearchView.OnQueryChangeListener {

        override fun onSearchTextChanged(oldQuery: String, newQuery: String?) {
            if (!isDisposed && newQuery != null && newQuery.length >= minQueryLength) {
                observer.onNext(newQuery)
            }
        }

        override fun onDispose() {
            view.setOnQueryChangeListener(null)
        }
    }
}
