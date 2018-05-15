package com.travlog.android.apps.libs.rx.views

import com.arlib.floatingsearchview.FloatingSearchView
import com.jakewharton.rxbinding2.InitialValueObservable
import com.travlog.android.apps.libs.rx.operators.QueryObservable

object RxFloatingSearchView {

    fun queryChanges(
            view: FloatingSearchView, characterLimit: Int = 1): InitialValueObservable<CharSequence> {
        checkNotNull(view, "view == null")
        return QueryObservable(view, characterLimit)
    }

    fun checkNotNull(value: Any?, message: String) {
        if (value == null) {
            throw NullPointerException(message)
        }
    }
}
