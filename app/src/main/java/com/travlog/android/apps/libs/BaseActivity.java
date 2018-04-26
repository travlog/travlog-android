package com.travlog.android.apps.libs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Pair;
import android.view.MenuItem;

import com.travlog.android.apps.ApplicationComponent;
import com.travlog.android.apps.TravlogApplication;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.libs.utils.BundleUtils;
import com.travlog.android.apps.ui.data.ActivityResult;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class BaseActivity<ViewModelType extends ActivityViewModel> extends RxAppCompatActivity
        implements ActivityLifeCycleType {

    private final PublishSubject<Void> back = PublishSubject.create();
    private static final String VIEW_MODEL_KEY = "viewModel";
    private final CompositeDisposable disposables = new CompositeDisposable();
    protected ViewModelType viewModel;

    /**
     * Get viewModel.
     */
    public ViewModelType viewModel() {
        return this.viewModel;
    }

    /**
     * Sends activity result data to the view model.
     */
    @CallSuper
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        this.viewModel.activityResult(ActivityResult.create(requestCode, resultCode, intent));
    }

    @CallSuper
    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate %s", this.toString());

        assignViewModel(savedInstanceState);

        this.viewModel.intent(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            back();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * Called when an activity is set to `singleTop` and it is relaunched while at the top of the activity stack.
     */
    @CallSuper
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.viewModel.intent(intent);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart %s", this.toString());

        this.back
                .compose(bindUntilEvent(ActivityEvent.STOP))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> goBack());
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume %s", this.toString());

        assignViewModel(null);
        if (this.viewModel != null) {
            this.viewModel.onResume(this);
        }
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("onPause %s", this.toString());

        if (this.viewModel != null) {
            this.viewModel.onPause();
        }
    }

    @CallSuper
    @Override
    protected void onStop() {
        super.onStop();
        Timber.d("onStop %s", this.toString());
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy %s", this.toString());

        this.disposables.clear();

        if (isFinishing()) {
            if (this.viewModel != null) {
                ActivityViewModelManager.getInstance().destroy(this.viewModel);
                this.viewModel = null;
            }
        }
    }

    /**
     * @deprecated Use {@link #back()} instead.
     * <p>
     * In rare situations, onBackPressed can be triggered after {@link #onSaveInstanceState(Bundle)} has been called.
     * This causes an {@link IllegalStateException} in the fragment manager's `checkStateLoss` method, because the
     * UI state has changed after being saved. The sequence of events might look like this:
     * <p>
     * onSaveInstanceState -> onStop -> onBackPressed
     * <p>
     * To avoid that situation, we need to ignore calls to `onBackPressed` after the activity has been saved. Since
     * the activity is stopped after `onSaveInstanceState` is called, we can create an observable of back events,
     * and a disposables that calls super.onBackPressed() only when the activity has not been stopped.
     */
    @CallSuper
    @Override
    @Deprecated
    public void onBackPressed() {
        back();
    }

    /**
     * Call when the user wants triggers a back event, e.g. clicking back in a toolbar or pressing the device back button.
     */
    public void back() {
        this.back.onNext(null);
    }

    /**
     * Override in subclasses for custom exit transitions. First item in pair is the enter animation,
     * second item in pair is the exit animation.
     */
    protected @Nullable
    Pair<Integer, Integer> exitTransition() {
        return null;
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(final @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d("onSaveInstanceState %s", this.toString());

        final Bundle viewModelEnvelope = new Bundle();
        if (this.viewModel != null) {
            ActivityViewModelManager.getInstance().save(this.viewModel, viewModelEnvelope);
        }

        outState.putBundle(VIEW_MODEL_KEY, viewModelEnvelope);
    }

    protected final void startActivityWithTransition(final @NonNull Intent intent, final @AnimRes int enterAnim,
                                                     final @AnimRes int exitAnim) {
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * Returns the {@link TravlogApplication} instance.
     */
    protected @NonNull
    TravlogApplication application() {
        return (TravlogApplication) getApplication();
    }

    /**
     * Convenience method to return a Dagger component.
     */
    protected @NonNull
    ApplicationComponent component() {
        return application().component();
    }

//    /**
//     * Returns the application's {@link Environment}.
//     */
//    protected @NonNull
//    Environment environment() {
//        return component().environment();
//    }

    /**
     * @deprecated Use {@link #bindToLifecycle()} or {@link #bindUntilEvent(ActivityEvent)} instead.
     */
    @Deprecated
    protected final void addDisposable(final @NonNull Disposable disposable) {
        this.disposables.add(disposable);
    }

    /**
     * Triggers a back press with an optional transition.
     */
    private void goBack() {
        super.onBackPressed();

        final Pair<Integer, Integer> exitTransitions = exitTransition();
        if (exitTransitions != null) {
            overridePendingTransition(exitTransitions.first, exitTransitions.second);
        }
    }

    private void assignViewModel(final @Nullable Bundle viewModelEnvelope) {
        if (this.viewModel == null) {
            final RequiresActivityViewModel annotation = getClass().getAnnotation(RequiresActivityViewModel.class);
            final Class<ViewModelType> viewModelClass = annotation == null ? null : (Class<ViewModelType>) annotation.value();
            if (viewModelClass != null) {
                this.viewModel = ActivityViewModelManager.getInstance().fetch(this,
                        viewModelClass,
                        BundleUtils.maybeGetBundle(viewModelEnvelope, VIEW_MODEL_KEY));
            }
        }
    }

    protected void setDisplayHomeAsUpEnabled(final boolean enabled) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enabled);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
