package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.jakewharton.rxbinding2.support.v4.view.RxViewPager;
import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.ui.adapters.SignUpPagerAdapter;
import com.travlog.android.apps.ui.widgets.NonSwipeableViewPager;
import com.travlog.android.apps.viewmodels.SignUpViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

@RequiresActivityViewModel(SignUpViewModel.class)
public class SignUpActivity extends BaseActivity<SignUpViewModel> {

    @BindView(R.id.close_button)
    View closeButton;
    @BindView(R.id.view_pager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.next)
    AppCompatButton nextButton;

    private SignUpPagerAdapter adapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_sign_up);

        adapter = new SignUpPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        RxView.clicks(closeButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.back());

        RxViewPager.pageSelections(viewPager)
                .doOnNext(__ -> setNextButtonEnabled(false))
                .filter(position -> position == adapter.getCount() - 1)
                .map(__ -> R.string.done)
                .compose(bindToLifecycle())
                .subscribe(this::setNextButtonText);

        RxView.clicks(nextButton)
                .map(__ -> viewPager.getCurrentItem() + 1)
                .compose(bindToLifecycle())
                .subscribe(nextPosition -> {
                    if (nextPosition == adapter.getCount()) {
                        viewModel.inputs.signUpClick();
                    } else {
                        viewPager.setCurrentItem(nextPosition);
                    }
                });

        viewModel.outputs.setNextButtonEnabled()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setNextButtonEnabled);

        addDisposable(
                viewModel.outputs.signUpSuccess()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::back));
    }

    private void setNextButtonText(final @StringRes int resId) {
        nextButton.setText(resId);
    }

    private void setNextButtonEnabled(final boolean enabled) {
        this.nextButton.setEnabled(enabled);
    }
}
