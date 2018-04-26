package com.travlog.android.apps.viewmodels.outputs;

import rx.Observable;

public interface NoteDetailsViewModelOutputs {

    Observable<String> loadBackground();

    Observable<String> setTitleText();

    Observable<String> setPeriodText();

    Observable<String> setLocationText();

    Observable<Integer> setMemberHorizontalScrollVisibility();

    Observable<Integer> setDateContainerVisibility();

    Observable<String> setMemoText();
}
