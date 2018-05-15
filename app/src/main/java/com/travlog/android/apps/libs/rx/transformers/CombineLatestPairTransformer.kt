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

