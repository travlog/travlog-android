package com.travlog.android.apps.libs

import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.models.User
import io.reactivex.Observable

abstract class CurrentUserType {

    /**
     * Call when a user has logged in. The implementation of `CurrentUserTypeKt` is responsible
     * for persisting the user and access token.
     */
    abstract fun login(newUser: User, accessToken: String)

    /**
     * Call when a user should be logged out.
     */
    abstract fun logout()

    /**
     * Get the logged in user's access token.
     */
    abstract fun getAccessToken(): String

    /**
     * Updates the persisted current user with a fresh, new user.
     */
    abstract fun refresh(freshUser: User)

    /**
     * Returns an observable representing the current user. It emits immediately
     * with the current user, and then again each time the user is updated.
     */
    abstract fun observable(): Observable<Optional<User>>

    /**
     * Returns the most recently emitted user from the user observable.
     *
     * @deprecated Prefer {@link #observable()}
     */
    @Deprecated("")
    abstract fun getUser(): User?

    /**
     * Returns a boolean that determines if there is a currently logged in user or not.
     *
     * @deprecated Prefer {@link #observable()}
     */
    @Deprecated("")
    fun exists(): Boolean {
        return getUser() != null
    }

    /**
     * Emits a boolean that determines if the user is logged in or not. The returned
     * observable will emit immediately with the logged in state, and then again
     * each time the current user is updated.
     */
    fun isLoggedIn(): Observable<Boolean> {
        return observable().map { it.isNotEmpty }
    }

    /**
     * Emits only values of a logged in user. The returned observable may never emit.
     */
    fun loggedInUser(): Observable<User?> {
        return observable().filter { it.isNotEmpty }.map { it.get() }
    }

    /**
     * Emits only values of a logged out user. The returned observable may never emit.
     */
    fun loggedOutUser(): Observable<Optional<User>> {
        return observable().filter { it.isEmpty }
    }
}