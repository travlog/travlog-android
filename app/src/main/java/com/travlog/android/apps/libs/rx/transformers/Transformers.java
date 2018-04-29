package com.travlog.android.apps.libs.rx.transformers;

import android.support.annotation.NonNull;

import com.travlog.android.apps.services.ApiException;
import com.travlog.android.apps.services.apiresponses.ErrorEnvelope;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public final class Transformers {
    private Transformers() {
    }

    /**
     * Prevents an observable from erroring by chaining `onErrorResumeNext`.
     */
    public static <T> NeverErrorTransformer<T> neverError() {
        return new NeverErrorTransformer<>();
    }

    /**
     * Prevents an observable from erroring by chaining `onErrorResumeNext`,
     * and any errors that occur will be piped into the supplied errors publish
     * subject. `null` values will never be sent to the publish subject.
     *
     * @deprecated Use {@link Observable#materialize()} instead.
     */
    @Deprecated
    public static <T> NeverErrorTransformer<T> pipeErrorsTo(final @NonNull PublishSubject<Throwable> errorSubject) {
        return new NeverErrorTransformer<>(errorSubject::onNext);
    }

    /**
     * Prevents an observable from erroring on any {@link ApiException} exceptions.
     */
    public static <T> NeverApiErrorTransformer<T> neverApiError() {
        return new NeverApiErrorTransformer<>();
    }


    /**
     * Prevents an observable from erroring on any {@link ApiException} exceptions,
     * and any errors that do occur will be piped into the supplied
     * errors publish subject. `null` values will never be sent to
     * the publish subject.
     *
     * @deprecated Use {@link Observable#materialize()} instead.
     */
    @Deprecated
    public static <T> NeverApiErrorTransformer<T> pipeApiErrorsTo(final @NonNull PublishSubject<ErrorEnvelope> errorSubject) {
        return new NeverApiErrorTransformer<>(errorSubject::onNext);
    }

    /**
     * Emits the latest value of the source observable whenever the `when`
     * observable emits.
     */
    public static <S, T> TakeWhenTransformer<S, T> takeWhen(final @NonNull Observable<T> when) {
        return new TakeWhenTransformer<>(when);
    }

    /**
     * Emits the latest values from two observables whenever either emits.
     */
    public static <S, T> CombineLatestPairTransformer<S, T> combineLatestPair(final @NonNull Observable<T> second) {
        return new CombineLatestPairTransformer<>(second);
    }
}
