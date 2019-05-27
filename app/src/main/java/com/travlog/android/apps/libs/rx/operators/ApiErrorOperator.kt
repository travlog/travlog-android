/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travlog.android.apps.libs.rx.operators

import com.google.gson.Gson
import com.travlog.android.apps.services.ApiException
import com.travlog.android.apps.services.ResponseException
import com.travlog.android.apps.services.apiresponses.Envelope

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

import java.io.IOException

import io.reactivex.FlowableOperator
import retrofit2.Response

/**
 * Takes a [retrofit2.Response], if it's successful send it to [Subscriber.onNext], otherwise
 * attempt to parse the error.
 *
 *
 * Errors that conform to the API's error format are converted into an [ApiException] exception and sent to
 * [Subscriber.onError], otherwise a more generic [ResponseException] is sent to [Subscriber.onError].
 *
 * @param <T> The response type.
</T> */
class ApiErrorOperator<T : Envelope>(private val gson: Gson) : FlowableOperator<T, Response<T>> {


    @Throws(Exception::class)
    override fun apply(observer: Subscriber<in T>): Subscriber<in Response<T>> {
        val gson = this.gson

        return object : org.reactivestreams.Subscriber<Response<T>> {
            override fun onSubscribe(s: Subscription) {
                observer.onSubscribe(s)
            }

            override fun onNext(tResponse: Response<T>) {
                if (!tResponse.isSuccessful) {
                    try {
                        val envelope = gson.fromJson(tResponse.errorBody()!!.string(), Envelope::class.java)
                        observer.onError(ApiException(envelope, tResponse))
                    } catch (e: IOException) {
                        observer.onError(ResponseException(tResponse))
                    }

                } else if (!tResponse.body()!!.success) {
                    val envelope = tResponse.body()

                    observer.onError(ApiException(envelope!!, tResponse))
                } else {
                    observer.onNext(tResponse.body())
                    observer.onComplete()
                }
            }

            override fun onError(t: Throwable) {
                observer.onError(t)
            }

            override fun onComplete() {
                observer.onComplete()
            }
        }
    }
}
