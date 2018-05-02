package com.travlog.android.apps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.FragmentLifeCycleType;
import com.travlog.android.apps.libs.rx.Optional;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;
import timber.log.Timber;

public class FragmentViewModel<ViewType extends FragmentLifeCycleType> {

    private final PublishSubject<Optional<ViewType>> viewChange = PublishSubject.create();
    private final Observable<ViewType> view = viewChange.filter(Optional::isNotEmpty).map(Optional::get);

    private final PublishSubject<Bundle> arguments = PublishSubject.create();

    private ViewType fragmentView;

    public ViewType view() {
        return fragmentView;
    }

    public FragmentViewModel(final @NonNull Environment environment) {
    }

    @CallSuper
    public void onCreate(final @NonNull Context context, final @Nullable Bundle savedInstanceState) {
        Timber.d("onCreate %s", this.toString());
    }

    /**
     * Takes bundle arguments from the view.
     */
    public void arguments(final @Nullable Bundle bundle) {
        this.arguments.onNext(bundle);
    }

    protected @NonNull
    Observable<Bundle> arguments() {
        return this.arguments;
    }

    @CallSuper
    public void onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState, final @NonNull ViewType view) {
        Timber.d("onCreateView %s", this.toString());
        onTakeView(view);
    }

    @CallSuper
    public void onStart() {
        Timber.d("onCreateView %s", this.toString());
    }

    @CallSuper
    public void onResume() {
        Timber.d("onCreateView %s", this.toString());
    }

    @CallSuper
    public void onPause() {
        Timber.d("onCreateView %s", this.toString());
    }

    @CallSuper
    public void onStop() {
        Timber.d("onCreateView %s", this.toString());
    }

    @CallSuper
    public void onDestroyView() {
        dropView();
        Timber.d("onCreateView %s", this.toString());
    }

    @CallSuper
    public void onDestroy() {
        Timber.d("onCreateView %s", this.toString());
    }

    @CallSuper
    public void onDetach() {
        Timber.d("onCreateView %s", this.toString());
    }

    private void onTakeView(final @NonNull ViewType view) {
        Timber.d("onTakeView %s %s", this.toString(), view.toString());
        fragmentView = view;
        this.viewChange.onNext(new Optional<>(view));
    }

    private void dropView() {
        Timber.d("dropView %s", this.toString());
        this.viewChange.onNext(new Optional<>(null));
    }

    public
    @NonNull
    <T> ObservableTransformer<T, T> bindToLifecycle() {
        return source -> source.takeUntil(
                view.switchMap(FragmentLifeCycleType::lifecycle)
                        .filter(FragmentEvent.DETACH::equals)
        );
    }
}
