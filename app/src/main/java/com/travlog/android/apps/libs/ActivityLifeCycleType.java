package com.travlog.android.apps.libs;

import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;

public interface ActivityLifeCycleType {

    Observable<ActivityEvent> lifecycle();
}
