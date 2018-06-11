package com.travlog.android.apps.viewmodels

import android.text.TextUtils

import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.ui.activities.MyPageActivity
import com.travlog.android.apps.viewmodels.errors.MyPageViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.MyPageViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.MyPageViewModelOutputs

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class MyPageViewModel(environment: Environment) : ActivityViewModel<MyPageActivity>(environment),
        MyPageViewModelInputs, MyPageViewModelOutputs, MyPageViewModelErrors {

    val inputs: MyPageViewModelInputs = this
    val outputs: MyPageViewModelOutputs = this
    val errors: MyPageViewModelErrors = this

    private val setProfilePicture = BehaviorSubject.create<String>()
    private val setUsernameText = BehaviorSubject.create<String>()

    init {

        environment.currentUser.loggedInUser()
                .map { user -> if (TextUtils.isEmpty(user.username)) "" else user.username }
                .compose(bindToLifecycle())
                .subscribe(setUsernameText)

        environment.currentUser.loggedInUser()
                .map { user -> if (TextUtils.isEmpty(user.profilePicture)) "" else user.profilePicture }
                .compose(bindToLifecycle())
                .subscribe(setProfilePicture)
    }

    override fun setProfilePicture(): Observable<String> {
        return setProfilePicture
    }

    override fun setUsernameText(): Observable<String> {
        return setUsernameText
    }
}
