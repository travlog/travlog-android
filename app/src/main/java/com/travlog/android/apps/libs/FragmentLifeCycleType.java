package com.travlog.android.apps.libs;

import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;

public interface FragmentLifeCycleType {

    Observable<FragmentEvent> lifecycle();
}
