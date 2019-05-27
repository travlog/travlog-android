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

import com.travlog.android.apps.services.apiresponses.Envelope
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import org.reactivestreams.Publisher


class NeverApiErrorTransformer<T> : FlowableTransformer<T, T> {
    private val errorAction: Consumer<Envelope>?

    constructor() {
        this.errorAction = null
    }

    constructor(errorAction: Consumer<Envelope>?) {
        this.errorAction = errorAction
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream
                .doOnError { e ->
                    val env = Envelope.fromThrowable(e)
                    if (env != null && this.errorAction != null) {
                        this.errorAction.accept(env)
                    }
                }
                .onErrorResumeNext(Function { Flowable.empty() })
    }
}
