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

package com.travlog.android.apps.services.interceptors

import com.travlog.android.apps.libs.CurrentUserType

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiRequestInterceptor(private val currentUser: CurrentUserType,
                            private val endpoint: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(request(chain.request()))
    }

    private fun request(initialRequest: Request): Request {
        //        if (!shouldIntercept(initialRequest)) {
        //            return initialRequest;
        //        }

        return initialRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + this.currentUser.getAccessToken())
                //                .url(url(initialRequest.url()))
                .url(initialRequest.url())
                .build()
    }

    //    private HttpUrl url(final @NonNull HttpUrl initialHttpUrl) {
    //        final HttpUrl.Builder builder = initialHttpUrl.newBuilder();
    //
    //        if (this.currentUser.exists()) {
    //            builder.setQueryParameter("client_id", this.currentUser.getUser().get().userId)
    //                    .setQueryParameter("oauth_token", this.currentUser.getAccessToken());
    //        }
    //
    //        return builder.build();
    //    }

    //    private boolean shouldIntercept(final @NonNull Request request) {
    //        return KSUri.isApiUri(Uri.parse(request.url().toString()), this.endpoint);
    //    }
}
