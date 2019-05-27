/*
 * Copyright 2019 fobidlim. All rights reserved.
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

package com.travlog.android.apps

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

val Activity.app: TravlogApplication get() = application as TravlogApplication

val Fragment.app: TravlogApplication get() = activity!!.app

fun FragmentActivity.getAppInjector(): ApplicationComponent = (app).component()

fun Fragment.getAppInjector(): ApplicationComponent = (app).component()

fun Activity.hideKeyboard() =
        hideKeyboard(when (currentFocus) {
            null -> View(this)
            else -> currentFocus
        })

fun Fragment.hideKeyboard() =
        view?.let { activity?.hideKeyboard(it) }

fun Context.hideKeyboard(view: View) =
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(view.windowToken, 0)
        }

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> FragmentActivity.withViewModel(
        viewModelFactory: ViewModelProvider.Factory,
        body: T.() -> Unit
): T {
    val viewModel = getViewModel<T>(viewModelFactory)
    viewModel.body()
    return viewModel
}

inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> Fragment.withViewModel(
        viewModelFactory: ViewModelProvider.Factory,
        body: T.() -> Unit
): T {
    val viewModel = getViewModel<T>(viewModelFactory)
    viewModel.body()
    return viewModel
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}
