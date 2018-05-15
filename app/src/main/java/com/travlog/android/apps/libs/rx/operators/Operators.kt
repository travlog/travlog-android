package com.travlog.android.apps.libs.rx.operators

import com.google.gson.Gson
import com.travlog.android.apps.services.ApiException
import com.travlog.android.apps.services.ResponseException
import com.travlog.android.apps.services.apiresponses.Envelope

import org.reactivestreams.Subscriber

object Operators {

    /**
     * When a response errors, send an [ApiException] or [ResponseException] to
     * [Subscriber.onError], otherwise send the response to [Subscriber.onNext].
     */
    fun <T : Envelope> apiError(gson: Gson): ApiErrorOperator<T> {
        return ApiErrorOperator(gson)
    }
}
