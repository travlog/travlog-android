package com.travlog.android.apps.libs

import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable

interface ActivityLifeCycleType {

    fun lifecycle(): Observable<ActivityEvent>
}
