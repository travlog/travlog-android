package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Observable

interface MyPageViewModelOutputs {

    fun setProfilePicture(): Observable<String>

    fun setUsernameText(): Observable<String>
}
