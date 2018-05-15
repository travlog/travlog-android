package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Observable

interface DestinationViewModelOutputs {

    fun setLocationText(): Observable<String>
}
