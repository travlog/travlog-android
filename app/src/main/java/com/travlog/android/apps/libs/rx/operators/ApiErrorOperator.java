package com.travlog.android.apps.libs.rx.operators;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.travlog.android.apps.services.ApiException;
import com.travlog.android.apps.services.ResponseException;
import com.travlog.android.apps.services.apiresponses.Envelope;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.FlowableOperator;
import retrofit2.Response;

/**
 * Takes a {@link retrofit2.Response}, if it's successful send it to {@link Subscriber#onNext}, otherwise
 * attempt to parse the error.
 * <p>
 * Errors that conform to the API's error format are converted into an {@link ApiException} exception and sent to
 * {@link Subscriber#onError}, otherwise a more generic {@link ResponseException} is sent to {@link Subscriber#onError}.
 *
 * @param <T> The response type.
 */
public final class ApiErrorOperator<T extends Envelope> implements FlowableOperator<T, Response<T>> {

    private final Gson gson;

    public ApiErrorOperator(final @NonNull Gson gson) {
        this.gson = gson;
    }


    @Override
    public Subscriber<? super Response<T>> apply(Subscriber<? super T> observer) throws Exception {
        final Gson gson = this.gson;

        return new org.reactivestreams.Subscriber<Response<T>>() {
            @Override
            public void onSubscribe(Subscription s) {
                observer.onSubscribe(s);
            }

            @Override
            public void onNext(Response<T> tResponse) {
                if (!tResponse.isSuccessful()) {
                    try {
                        final Envelope envelope = gson.fromJson(tResponse.errorBody().string(), Envelope.class);
                        observer.onError(new ApiException(envelope, tResponse));
                    } catch (final @NonNull IOException e) {
                        observer.onError(new ResponseException(tResponse));
                    }
                } else if (!tResponse.body().success) {
                    final Envelope envelope = tResponse.body();

                    observer.onError(new ApiException(envelope, tResponse));
                } else {
                    observer.onNext(tResponse.body());
                    observer.onComplete();
                }
            }

            @Override
            public void onError(Throwable t) {
                observer.onError(t);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        };
    }
}
