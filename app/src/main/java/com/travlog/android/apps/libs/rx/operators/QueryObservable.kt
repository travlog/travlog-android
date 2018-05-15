package com.travlog.android.apps.libs.rx.operators

import com.arlib.floatingsearchview.FloatingSearchView
import com.jakewharton.rxbinding2.InitialValueObservable

import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class QueryObservable @JvmOverloads constructor(private val view: FloatingSearchView, private val minQueryLength: Int = 1) : InitialValueObservable<CharSequence>() {

    override fun subscribeListener(observer: Observer<in CharSequence>) {
        val listener = Listener(view, observer, minQueryLength)
        observer.onSubscribe(listener)
        view.setOnQueryChangeListener(listener)
    }

    override fun getInitialValue(): CharSequence {
        return view.query
    }

    internal class Listener(private val view: FloatingSearchView, private val observer: Observer<in CharSequence>, private val minQueryLength: Int) : MainThreadDisposable(), FloatingSearchView.OnQueryChangeListener {

        override fun onSearchTextChanged(oldQuery: String, newQuery: String?) {
            if (!isDisposed && newQuery != null && newQuery.length >= minQueryLength) {
                observer.onNext(newQuery)
            }
        }

        override fun onDispose() {
            view.setOnQueryChangeListener(null)
        }
    }
}
