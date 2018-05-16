package com.travlog.android.apps.viewmodels

import android.app.Activity.RESULT_OK
import com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.ui.IntentKey.PREDICTION
import com.travlog.android.apps.ui.activities.DestinationActivity
import com.travlog.android.apps.viewmodels.errors.DestinationViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.DestinationViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.DestinationViewModelOutputs
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class DestinationViewModel(environment: Environment) : ActivityViewModel<DestinationActivity>(environment),
        DestinationViewModelInputs, DestinationViewModelOutputs, DestinationViewModelErrors {

    private val setLocationText = BehaviorSubject.create<String>()

    val inputs: DestinationViewModelInputs = this
    val outputs: DestinationViewModelOutputs = this
    val errors: DestinationViewModelErrors = this

    init {
        activityResult()
                .filter { it.requestCode == SEARCH_LOCATION }
                .filter { it.resultCode == RESULT_OK }
                .map { it.intent }
                .map { it.getParcelableExtra(PREDICTION) as Prediction }
                .map { it.structuredFormatting.mainText }
                .subscribe(setLocationText)
    }

    override fun setLocationText(): Observable<String> {
        return setLocationText
    }
}
