package com.travlog.android.apps.libs.rx.transformers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.reactivestreams.Publisher;

import java.util.function.Consumer;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;

public final class NeverErrorTransformer<T> implements FlowableTransformer<T, T> {

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
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream
                .doOnError(e -> {
                    if (this.errorAction != null) {
                        this.errorAction.accept(e);
                    }
                })
                .onErrorResumeNext(Flowable.empty());
    }
}
