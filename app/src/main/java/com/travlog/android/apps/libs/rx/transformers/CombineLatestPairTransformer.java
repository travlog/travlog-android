package com.travlog.android.apps.libs.rx.transformers;

import android.support.annotation.NonNull;
import android.util.Pair;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public final class CombineLatestPairTransformer<S, T> implements ObservableTransformer<S, Pair<S, T>> {

    @NonNull
    private final Observable<T> second;

    public CombineLatestPairTransformer(final @NonNull Observable<T> second) {
        this.second = second;
    }

    @Override
    @NonNull
    public ObservableSource<Pair<S, T>> apply(Observable<S> upstream) {
        return Observable.combineLatest(upstream, second, Pair::new);
    }
}

