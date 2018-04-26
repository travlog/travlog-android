package com.travlog.android.apps.viewmodels.outputs;

import com.travlog.android.apps.models.Destination;

import java.util.ArrayList;

import rx.Observable;

public interface EditNoteViewModelOutputs {

    Observable<String> setTitleText();

    Observable<ArrayList<Destination>> startEditDestinationActivity();

    Observable<String> setPeriodText();

    Observable<Destination> addDestinationView();
}
