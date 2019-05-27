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

package com.travlog.android.apps.libs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import androidx.annotation.AnimRes
import androidx.annotation.CallSuper
import butterknife.ButterKnife
import com.travlog.android.apps.ApplicationComponent
import com.travlog.android.apps.TravlogApplication
import com.travlog.android.apps.ui.data.ActivityResult
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.CompletableSubject
import timber.log.Timber

@SuppressLint("Registered")
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

        this.viewModel?.intent(intent)
        this.viewModel?.onCreate(this, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId == android.R.id.home -> {
                back()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        viewModel?.intent(intent)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        Timber.d("onStart %s", this.toString())

        addDisposable(
                back
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { goBack() })
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        Timber.d("onResume %s", this.toString())

        viewModel?.onResume(this)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        Timber.d("onPause %s", this.toString())

        viewModel?.onPause()
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
                this.viewModel = null
            }
        }
    }


    @CallSuper
    @Deprecated("Use {@link #setResultAndBack()} instead.\n" +
            "      <p>\n" +
            "      In rare situations, onBackPressed can be triggered after {@link #onSaveInstanceState(Bundle)} has been called.\n" +
            "      This causes an {@link IllegalStateException} in the fragment manager's `checkStateLoss` method, because the\n" +
            "      UI state has changed after being saved. The sequence of events might look like this:\n" +
            "      <p>\n" +
            "      onSaveInstanceState -> onStop -> onBackPressed\n" +
            "      <p>\n" +
            "      To avoid that situation, we need to ignore calls to `onBackPressed` after the activity has been saved. Since\n" +
            "      the activity is stopped after `onSaveInstanceState` is called, we can create an observable of setResultAndBack events,\n" +
            "      and a disposables that calls super.onBackPressed() only when the activity has not been stopped.", ReplaceWith("setResultAndBack()"))
    override fun onBackPressed() {
        back()
    }

    /**
     * Call when the user wants triggers a setResultAndBack event, e.g. clicking setResultAndBack in a toolbar or pressing the device setResultAndBack button.
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

        outState.putBundle(VIEW_MODEL_KEY, viewModelEnvelope)
    }

    protected fun startActivityWithTransition(intent: Intent, @AnimRes enterAnim: Int,
                                              @AnimRes exitAnim: Int) {
        startActivity(intent).run {
            overridePendingTransition(enterAnim, exitAnim)
        }
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
     * Triggers a setResultAndBack press with an optional transition.
     */
    private fun goBack() {
        super.onBackPressed()

        exitTransition()?.let {
            overridePendingTransition(it.first, it.second)
        }
    }

    protected fun setDisplayHomeAsUpEnabled(enabled: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
    }
}