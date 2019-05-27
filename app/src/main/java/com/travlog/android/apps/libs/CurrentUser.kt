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

import com.facebook.login.LoginManager
import com.google.gson.Gson
import com.travlog.android.apps.libs.db.realm.RealmHelper
import com.travlog.android.apps.libs.perferences.StringPreferenceType
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.models.User
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class CurrentUser(private var accessTokenPreference: StringPreferenceType, private var userPreference: StringPreferenceType) : CurrentUserType() {

    private val user = BehaviorSubject.create<Optional<User>>()

    init {
        val gson = Gson()

        this.user
                .skip(1)
                .filter { it.isNotEmpty }
                .map { it.get() }
                .subscribe { userPreference.set(gson.toJson(it, User::class.java)) }

        this.user.onNext(Optional(gson.fromJson(userPreference.get(), User::class.java)))
    }

    override fun getUser(): User? {
        return this.user.value?.get()
    }

    override fun getAccessToken(): String {
        return this.accessTokenPreference.get() ?: ""
    }

    override fun login(newUser: User, accessToken: String) {
        Timber.d("Login user %s", newUser.name)

        this.accessTokenPreference.set(accessToken)
        this.user.onNext(Optional(newUser))
    }

    override fun logout() {
        Timber.d("Logout current user")

        this.userPreference.delete()
        this.accessTokenPreference.delete()
        this.user.onNext(Optional(null))
        LoginManager.getInstance().logOut()
        RealmHelper.deleteAllAsync()
    }

    override fun refresh(freshUser: User) {
        this.user.onNext(Optional(freshUser))
    }

    override fun observable(): Observable<Optional<User>> {
        return this.user
    }
}