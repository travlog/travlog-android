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
