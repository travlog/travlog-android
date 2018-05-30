package com.travlog.android.apps.viewmodels.outputs

import com.travlog.android.apps.models.Destination
import io.reactivex.Observable

interface DestinationViewModelOutputs {

    fun setLocationText(): Observable<String>

    fun setStartDateText(): Observable<String>

    fun setEndDateText(): Observable<String>

    fun setSaveButtonEnabled(): Observable<Boolean>

    fun setResultAndBack(): Observable<Destination>
}
