package com.travlog.android.apps.libs

import android.content.Intent
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.annotation.CallSuper
import android.util.Pair
import android.view.MenuItem
import butterknife.ButterKnife
import com.travlog.android.apps.ApplicationComponent
import com.travlog.android.apps.TravlogApplication
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.maybeGetBundle
import com.travlog.android.apps.ui.data.ActivityResult
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.CompletableSubject
import timber.log.Timber

open class BaseActivity<ViewModelType : ActivityViewModel<*>> : RxAppCompatActivity(), ActivityLifeCycleType {

    private val back = CompletableSubject.create()
    private val VIEW_MODEL_KEY = "viewModel"
    private val disposables = CompositeDisposable()
    protected var viewModel: ViewModelType? = null

    /**
     * Get viewModel.
     */
    fun viewModel(): ViewModelType? {
        return this.viewModel
    }

    /**
     * Sends activity result data to the view model.
     */
    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        this.viewModel?.activityResult(ActivityResult.create(requestCode, resultCode, intent))
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate %s", this.toString())

        assignViewModel(savedInstanceState)

        this.viewModel?.intent(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            back()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ButterKnife.bind(this)
    }

    /**
     * Called when an activity is set to `singleTop` and it is relaunched while at the top of the activity stack.
     */
    @CallSuper
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.viewModel?.intent(intent)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        Timber.d("onStart %s", this.toString())

        addDisposable(
                this.back
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { this.goBack() })
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Timber.d("onResume %s", this.toString())

        assignViewModel(null)
        this.viewModel?.onResume(this)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        Timber.d("onPause %s", this.toString())

        this.viewModel?.onPause()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        Timber.d("onStop %s", this.toString())
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy %s", this.toString())

        this.disposables.clear()

        if (isFinishing) {
            if (this.viewModel != null) {
                ActivityViewModelManager.instance.destroy(this.viewModel!!)
                this.viewModel = null
            }
        }
    }


    @CallSuper
    @Deprecated("Use {@link #back()} instead.\n" +
            "      <p>\n" +
            "      In rare situations, onBackPressed can be triggered after {@link #onSaveInstanceState(Bundle)} has been called.\n" +
            "      This causes an {@link IllegalStateException} in the fragment manager's `checkStateLoss` method, because the\n" +
            "      UI state has changed after being saved. The sequence of events might look like this:\n" +
            "      <p>\n" +
            "      onSaveInstanceState -> onStop -> onBackPressed\n" +
            "      <p>\n" +
            "      To avoid that situation, we need to ignore calls to `onBackPressed` after the activity has been saved. Since\n" +
            "      the activity is stopped after `onSaveInstanceState` is called, we can create an observable of back events,\n" +
            "      and a disposables that calls super.onBackPressed() only when the activity has not been stopped.")
    override fun onBackPressed() {
        back()
    }

    /**
     * Call when the user wants triggers a back event, e.g. clicking back in a toolbar or pressing the device back button.
     */
    fun back() {
        this.back.onComplete()
    }

    /**
     * Override in subclasses for custom exit transitions. First item in pair is the enter animation,
     * second item in pair is the exit animation.
     */
    protected open fun exitTransition(): Pair<Int, Int>? {
        return null
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.d("onSaveInstanceState %s", this.toString())

        val viewModelEnvelope = Bundle()
        if (this.viewModel != null) {
            ActivityViewModelManager.instance.save(this.viewModel!!, viewModelEnvelope)
        }

        outState.putBundle(VIEW_MODEL_KEY, viewModelEnvelope)
    }

    protected fun startActivityWithTransition(intent: Intent, @AnimRes enterAnim: Int,
                                              @AnimRes exitAnim: Int) {
        startActivity(intent)
        overridePendingTransition(enterAnim, exitAnim)
    }

    /**
     * Returns the [TravlogApplication] instance.
     */
    protected fun application(): TravlogApplication {
        return application as TravlogApplication
    }

    /**
     * Convenience method to return a Dagger component.
     */
    protected fun component(): ApplicationComponent? {
        return application().component()
    }

//    /**
//     * Returns the application's {@link Environment}.
//     */
//    protected @NonNull
//    Environment environment() {
//        return component().environment();
//    }

    protected fun addDisposable(disposable: Disposable) {
        this.disposables.add(disposable)
    }

    /**
     * Triggers a back press with an optional transition.
     */
    private fun goBack() {
        super.onBackPressed()

        val exitTransitions = exitTransition()
        if (exitTransitions != null) {
            overridePendingTransition(exitTransitions.first, exitTransitions.second)
        }
    }

    private fun assignViewModel(viewModelEnvelope: Bundle?) {
        if (this.viewModel == null) {
            val viewModelClass = javaClass.getAnnotation(RequiresActivityViewModel::class.java)?.value
            if (viewModelClass != null) {
                this.viewModel = ActivityViewModelManager.instance.fetch(this,
                        viewModelClass,
                        maybeGetBundle(viewModelEnvelope, VIEW_MODEL_KEY))
            }
        }
    }

    protected fun setDisplayHomeAsUpEnabled(enabled: Boolean) {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(enabled)
    }
}