package com.travlog.android.apps.libs.rx.transformers;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public final class TakeWhenTransformer<S, T> implements ObservableTransformer<S, S> {

    @NonNull
    private final Observable<T> when;

    public TakeWhenTransformer(final @NonNull Observable<T> when) {
        this.when = when;
    }

    @Override
    @NonNull
    public ObservableSource<S> apply(Observable<S> upstream) {
        return when.withLatestFrom(upstream, (__, x) -> x);
    }
}
