package com.travlog.android.apps.libs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.ui.data.ActivityResult
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.realm.Realm
import timber.log.Timber

open class ActivityViewModel<ViewType : ActivityLifeCycleType>(environment: Environment) : ViewModel() {

    private val viewChange = PublishSubject.create<Optional<ViewType>>()
    private val view = viewChange.filter({ it.isNotEmpty }).map({ it.get() })
    private val disposables = CompositeDisposable()
    private val activityResult = PublishSubject.create<ActivityResult>()
    private val intent = PublishSubject.create<Intent>()

    protected lateinit var realm: Realm

    /**
     * Takes activity result data from the activity.
     */
    fun activityResult(activityResult: ActivityResult) {
        this.activityResult.onNext(activityResult)
    }

    /**
     * Takes intent data from the view.
     */
    fun intent(intent: Intent) {
        this.intent.onNext(intent)
    }

    @CallSuper
    fun onCreate(context: Context, savedInstanceState: Bundle?) {
        Timber.d("onCreate %s", this.toString())
        dropView()
        realm = Realm.getDefaultInstance()
    }

    @CallSuper
    open fun onResume(view: Any) {
        Timber.d("onResume %s", this.toString())
        onTakeView(view)
    }

    @CallSuper
    open fun onPause() {
        Timber.d("onPause %s", this.toString())
        dropView()
    }

    @CallSuper
    open fun onDestroy() {
        Timber.d("onDestroy %s", this.toString())

        this.disposables.clear()
        this.viewChange.onComplete()
        realm.close()
    }

    private fun onTakeView(view: Any) {
        Timber.d("onTakeView %s %s", this.toString(), view.toString())
        this.viewChange.onNext(Optional(if (view is ActivityLifeCycleType) view as ViewType else null))
    }

    private fun dropView() {
        Timber.d("dropView %s", this.toString())
        this.viewChange.onNext(Optional(null))
    }

    protected fun activityResult(): Observable<ActivityResult> {
        return this.activityResult
    }

    protected fun intent(): Observable<Intent> {
        return this.intent
    }

    /**
     * By composing this transformer with an observable you guarantee that every observable in your view model
     * will be properly completed when the view model completes.
     *
     *
     * It is required that *every* observable in a view model do `.compose(bindToLifecycle())` before calling
     * `subscribe`.
     */
    protected fun <T> bindToLifecycle(): ObservableTransformer<T, T> {
        return ObservableTransformer { source ->
            source.takeUntil(this.view.switchMap { v -> v.lifecycle().map<Pair<ViewType, ActivityEvent>> { Pair.create(v, it) } }
                    .filter { isFinished(it.first, it.second) })
        }
    }

    /**
     * Determines from a view and lifecycle event if the view's life is over.
     */
    private fun isFinished(view: ViewType, event: ActivityEvent): Boolean {
        return if (view is BaseActivity<*>) {
            event == ActivityEvent.DESTROY && (view as BaseActivity<*>).isFinishing
        } else event == ActivityEvent.DESTROY
    }
}