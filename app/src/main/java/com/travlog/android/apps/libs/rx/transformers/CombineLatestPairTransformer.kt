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

package com.travlog.android.apps.libs.rx.transformers

import android.util.Pair

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

class CombineLatestPairTransformer<S, T>(private val second: Observable<T>) : ObservableTransformer<S, Pair<S, T>> {

    override fun apply(upstream: Observable<S>): ObservableSource<Pair<S, T>> {
        return Observable.combineLatest(upstream, second, BiFunction<S, T, Pair<S, T>> { first, second -> Pair(first, second) })
    }
}

