package com.travlog.android.apps.libs.rx.transformers

import com.travlog.android.apps.services.ApiException
import com.travlog.android.apps.services.apiresponses.Envelope

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

object Transformers {

    /**
     * Prevents an observable from erroring by chaining `onErrorResumeNext`.
     */
    fun <T> neverError(): NeverErrorTransformer<T> {
        return NeverErrorTransformer()
    }

    /**
     * Prevents an observable from erroring by chaining `onErrorResumeNext`,
     * and any errors that occur will be piped into the supplied errors publish
     * subject. `null` values will never be sent to the publish subject.
     *
     */
    @Deprecated("Use {@link Observable#materialize()} instead.")
    fun <T> pipeErrorsTo(errorSubject: PublishSubject<Throwable>): NeverErrorTransformer<T> {
        return NeverErrorTransformer(Consumer { errorSubject.onNext(it) })
    }

    /**
     * Prevents an observable from erroring on any [ApiException] exceptions.
     */
    fun <T> neverApiError(): NeverApiErrorTransformer<T> {
        return NeverApiErrorTransformer()
    }


    /**
     * Prevents an observable from erroring on any [ApiException] exceptions,
     * and any errors that do occur will be piped into the supplied
     * errors publish subject. `null` values will never be sent to
     * the publish subject.
     *
     */
    @Deprecated("Use {@link Observable#materialize()} instead.")
    fun <T> pipeApiErrorsTo(errorSubject: PublishSubject<Envelope>): NeverApiErrorTransformer<T> {
        return NeverApiErrorTransformer(Consumer<Envelope> { errorSubject.onNext(it) })
    }

    /**
     * Emits the latest value of the source observable whenever the `when`
     * observable emits.
     */
    fun <S, T> takeWhen(`when`: Observable<T>): TakeWhenTransformer<S, T> {
        return TakeWhenTransformer(`when`)
    }

    /**
     * Emits the latest values from two observables whenever either emits.
     */
    fun <S, T> combineLatestPair(second: Observable<T>): CombineLatestPairTransformer<S, T> {
        return CombineLatestPairTransformer(second)
    }
}
