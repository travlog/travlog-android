package com.travlog.android.apps.services.interceptors;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.CurrentUserType;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class ApiRequestInterceptor implements Interceptor {

    private final CurrentUserType currentUser;
    private final String endpoint;

    public ApiRequestInterceptor(final @NonNull CurrentUserType currentUser,
                                 final @NonNull String endpoint) {

        this.currentUser = currentUser;
        this.endpoint = endpoint;
    }

    @Override
    public Response intercept(final @NonNull Chain chain) throws IOException {
        return chain.proceed(request(chain.request()));
    }

    private Request request(final @NonNull Request initialRequest) {
//        if (!shouldIntercept(initialRequest)) {
//            return initialRequest;
//        }

        return initialRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + this.currentUser.getAccessToken())
                .url(initialRequest.url())
                .build();
    }

//    private boolean shouldIntercept(final @NonNull Request request) {
//        return KSUri.isApiUri(Uri.parse(request.url().toString()), this.endpoint);
//    }
}
