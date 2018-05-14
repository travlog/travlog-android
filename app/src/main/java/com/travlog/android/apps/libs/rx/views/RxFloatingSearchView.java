package com.travlog.android.apps.libs.rx.views;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.travlog.android.apps.libs.rx.operators.QueryObservable;

public final class RxFloatingSearchView {

    @CheckResult
    @NonNull
    public static InitialValueObservable<CharSequence> queryChanges(
            @NonNull FloatingSearchView view) {
        return queryChanges(view, 1);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<CharSequence> queryChanges(
            @NonNull FloatingSearchView view, int characterLimit) {
        checkNotNull(view, "view == null");
        return new QueryObservable(view, characterLimit);
    }

    public static void checkNotNull(Object value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
    }
}
