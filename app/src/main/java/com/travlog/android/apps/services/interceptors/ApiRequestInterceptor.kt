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
