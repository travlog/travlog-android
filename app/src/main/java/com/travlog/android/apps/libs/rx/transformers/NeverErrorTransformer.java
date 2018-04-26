package com.travlog.android.apps.libs.rx.transformers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.function.Consumer;

import rx.Observable;

public final class NeverErrorTransformer<T> implements Observable.Transformer<T, T> {

    private final @Nullable
    Consumer<Throwable> errorAction;

    protected NeverErrorTransformer() {
        this.errorAction = null;
    }

    protected NeverErrorTransformer(final @Nullable Consumer<Throwable> errorAction) {
        this.errorAction = errorAction;
    }

    @Override
    @NonNull
    public Observable<T> call(final @NonNull Observable<T> source) {
        return source
                .doOnError(e -> {
                    if (this.errorAction != null) {
                        this.errorAction.accept(e);
                    }
                })
                .onErrorResumeNext(Observable.empty());
    }
}
