package com.travlog.android.apps.libs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travlog.android.apps.FragmentViewModel;
import com.travlog.android.apps.libs.qualifiers.RequiresFragmentViewModel;
import com.travlog.android.apps.libs.utils.BundleUtilsKt;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class BaseFragment<ViewModelType extends FragmentViewModel> extends RxFragment
        implements FragmentLifeCycleType {

    private static final String VIEW_MODEL_KEY = "viewModel";
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Unbinder unbinder;
    protected ViewModelType viewModel;

    public ViewModelType viewModel() {
        return this.viewModel;
    }

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("onAttach %s", this.toString());
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate %s", this.toString());
        assignViewModel(savedInstanceState);
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView %s", this.toString());
        if (viewModel != null)
            viewModel.onCreateView(inflater, container, savedInstanceState, this);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected View setContentView(@LayoutRes int layoutResID) {
        final View view = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart %s", this.toString());
        if (viewModel != null)
            viewModel.onStart();
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume %s", this.toString());
        if (viewModel != null)
            viewModel.onResume();
    }

    @CallSuper
    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause %s", this.toString());
        if (viewModel != null)
            viewModel.onPause();
    }

    @CallSuper
    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop %s", this.toString());
        if (viewModel != null)
            viewModel.onStop();
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.d("onDestroyView %s", this.toString());
        if (viewModel != null)
            viewModel.onDestroyView();
        unbinder.unbind();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy %s", this.toString());
        if (viewModel != null)
            viewModel.onDestroy();
    }

    @CallSuper
    @Override
    public void onDetach() {
        Timber.d("onDetach %s", this.toString());
        super.onDetach();

        if (getActivity().isFinishing()) {
            if (viewModel != null) {
                viewModel.onDetach();
            }
        }
    }

    private void assignViewModel(final @Nullable Bundle viewModelEnvelope) {
        if (viewModel == null) {
            final RequiresFragmentViewModel annotation = getClass().getAnnotation(RequiresFragmentViewModel.class);
            final Class<ViewModelType> ViewModelTypeClass = annotation == null ? null : (Class<ViewModelType>) annotation.value();
            if (ViewModelTypeClass != null) {
                viewModel = FragmentViewModelManager.getInstance().fetch(getContext(),
                        ViewModelTypeClass,
                        BundleUtilsKt.maybeGetBundle(viewModelEnvelope, VIEW_MODEL_KEY));
            }
        }
    }
}
