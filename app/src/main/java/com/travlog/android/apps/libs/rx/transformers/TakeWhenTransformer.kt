package com.travlog.android.apps.libs.rx.transformers

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

class TakeWhenTransformer<S, T>(private val `when`: Observable<T>) : ObservableTransformer<S, S> {

    override fun apply(upstream: Observable<S>): ObservableSource<S> {
        return `when`.withLatestFrom(upstream, BiFunction { _, x -> x })
    }
}
