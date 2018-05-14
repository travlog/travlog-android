package com.travlog.android.apps.viewmodels;

import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ActivityViewModel;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.models.Prediction;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.ui.activities.SearchLocationActivity;
import com.travlog.android.apps.viewmodels.errors.SearchLocationViewModelErrors;
import com.travlog.android.apps.viewmodels.inputs.SearchLocationViewModelInputs;
import com.travlog.android.apps.viewmodels.outputs.SearchLocationViewModelOutputs;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static com.travlog.android.apps.libs.rx.transformers.Transformers.neverError;

public class SearchLocationViewModel extends ActivityViewModel<SearchLocationActivity>
        implements SearchLocationViewModelInputs, SearchLocationViewModelOutputs, SearchLocationViewModelErrors {

    private final ApiClientType apiClient;

    public SearchLocationViewModel(final @NonNull Environment environment) {
        super(environment);

        this.apiClient = environment.apiClient;

        query
                .debounce(200, TimeUnit.MILLISECONDS)
                .filter(query -> !query.isEmpty())
                .switchMap(query -> this.search(query)
                        .doOnSubscribe(disposable -> {

                        })
                        .doAfterTerminate(() -> {

                        }))
                .compose(bindToLifecycle())
                .subscribe(swapSuggestions);

        prediction
                .compose(bindToLifecycle())
                .subscribe(setResultAndBack);
    }

    private @NonNull
    Observable<List<Prediction>> search(final @NonNull String query) {
        return apiClient.searchLocation(query)
                .compose(neverError())
                .toObservable();
    }

    private final PublishSubject<String> query = PublishSubject.create();
    private final PublishSubject<Prediction> prediction = PublishSubject.create();

    private final BehaviorSubject<List<Prediction>> swapSuggestions = BehaviorSubject.create();
    private final BehaviorSubject<Prediction> setResultAndBack = BehaviorSubject.create();

    public final SearchLocationViewModelInputs inputs = this;
    public final SearchLocationViewModelOutputs outputs = this;
    public final SearchLocationViewModelErrors errors = this;

    @Override
    public void query(final @NonNull String q) {
        this.query.onNext(q);
    }

    @Override
    public void predictionClick(@NonNull Prediction prediction) {
        this.prediction.onNext(prediction);
    }

    @NonNull
    @Override
    public Observable<List<Prediction>> swapSuggestions() {
        return swapSuggestions;
    }

    @NonNull
    @Override
    public Observable<Prediction> setResultAndBack() {
        return setResultAndBack;
    }
}
