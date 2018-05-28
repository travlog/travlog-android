package com.travlog.android.apps

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.FragmentLifeCycleType
import com.travlog.android.apps.libs.rx.Optional
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

open class FragmentViewModel<ViewType : FragmentLifeCycleType>(environment: Environment) {

    private val viewChange = PublishSubject.create<Optional<ViewType>>()
    private val view = viewChange.filter({ it.isNotEmpty }).map { it.get() }

    private val arguments = PublishSubject.create<Bundle>()

    private var fragmentView: ViewType? = null

    fun view(): ViewType? {
        return fragmentView
    }

    @CallSuper
    fun onCreate(context: Context, savedInstanceState: Bundle?) {
        Timber.d("onCreate %s", this.toString())
    }

    /**
     * Takes bundle arguments from the view.
     */
    fun arguments(bundle: Bundle?) {
        this.arguments.onNext(bundle!!)
    }

    protected fun arguments(): Observable<Bundle> {
        return this.arguments
    }

    @CallSuper
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                     savedInstanceState: Bundle?, view: Any) {
        Timber.d("onCreateView %s", this.toString())
        onTakeView(view)
    }

    @CallSuper
    fun onStart() {
        Timber.d("onCreateView %s", this.toString())
    }

    @CallSuper
    fun onResume() {
        Timber.d("onCreateView %s", this.toString())
    }

    @CallSuper
    fun onPause() {
        Timber.d("onCreateView %s", this.toString())
    }

    @CallSuper
    fun onStop() {
        Timber.d("onCreateView %s", this.toString())
    }

    @CallSuper
    fun onDestroyView() {
        dropView()
        Timber.d("onCreateView %s", this.toString())
    }

    @CallSuper
    fun onDestroy() {
        Timber.d("onCreateView %s", this.toString())
    }

    @CallSuper
    fun onDetach() {
        Timber.d("onCreateView %s", this.toString())
    }

    private fun onTakeView(view: Any) {
        Timber.d("onTakeView %s %s", this.toString(), view.toString())

        if (view is FragmentLifeCycleType) {
            fragmentView = view as ViewType
            this.viewChange.onNext(Optional(view))
        }

    }

    private fun dropView() {
        Timber.d("dropView %s", this.toString())
        this.viewChange.onNext(Optional(null))
    }

    fun <T> bindToLifecycle(): ObservableTransformer<T, T> {
        return ObservableTransformer { source ->
            source.takeUntil(this.view.switchMap { it.lifecycle() }
                    .filter { FragmentEvent.DETACH == it })
        }
    }
}
