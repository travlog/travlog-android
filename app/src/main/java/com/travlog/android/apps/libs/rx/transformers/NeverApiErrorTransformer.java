package com.travlog.android.apps.libs.rx.transformers;

import android.support.annotation.Nullable;

import com.travlog.android.apps.services.apiresponses.Envelope;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Consumer;


public final class NeverApiErrorTransformer<T> implements FlowableTransformer<T, T> {
    private final @Nullable
    Consumer<Envelope> errorAction;

    protected NeverApiErrorTransformer() {
        this.errorAction = null;
    }

    protected NeverApiErrorTransformer(final @Nullable Consumer<Envelope> errorAction) {
        this.errorAction = errorAction;
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream
                .doOnError(e -> {
                    final Envelope env = Envelope.fromThrowable(e);
                    if (env != null && this.errorAction != null) {
                        this.errorAction.accept(env);
                    }
                })
                .onErrorResumeNext(e -> {
                    if (Envelope.fromThrowable(e) == null) {
                        return Flowable.error(e);
                    } else {
                        return Flowable.empty();
                    }
                });
    }
}
