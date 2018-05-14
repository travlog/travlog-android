package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.models.Prediction;
import com.travlog.android.apps.ui.activities.DestinationActivity;
import com.travlog.android.apps.viewmodels.errors.DestinationViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.DestinationViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.DestinationViewModelOutputs;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static android.app.Activity.RESULT_OK;
import static com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION;
import static com.travlog.android.apps.ui.IntentKey.PREDICTION;

public class DestinationViewModel extends ActivityViewModel<DestinationActivity>
        implements DestinationViewModelInputs, DestinationViewModelOutputs, DestinationViewModelErrors {

    public DestinationViewModel(final @NonNull Environment environment) {
        super(environment);

        activityResult()
                .filter(activityResult -> activityResult.requestCode == SEARCH_LOCATION)
                .filter(activityResult -> activityResult.resultCode == RESULT_OK)
                .map(activityResult -> activityResult.intent)
                .map(i -> (Prediction) i.getParcelableExtra(PREDICTION))
                .map(prediction -> prediction.structuredFormatting.mainText)
                .subscribe(setLocationText);
    }

    private final BehaviorSubject<String> setLocationText = BehaviorSubject.create();

    public final DestinationViewModelInputs inputs = this;
    public final DestinationViewModelOutputs outputs = this;
    public final DestinationViewModelErrors errors = this;

    @NonNull
    @Override
    public Observable<String> setLocationText() {
        return setLocationText;
    }
}
