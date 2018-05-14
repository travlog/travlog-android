package com.travlog.android.apps.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;

import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.DestinationViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.travlog.android.apps.libs.ActivityRequestCodes.SEARCH_LOCATION;

@RequiresActivityViewModel(DestinationViewModel.class)
public class DestinationActivity extends BaseActivity<DestinationViewModel> {

    @BindView(R.id.start_date)
    MaterialButton startDateButton;
    @BindView(R.id.end_date)
    MaterialButton endDateButton;
    @BindView(R.id.location)
    MaterialButton locationButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_destination);

        RxView.clicks(locationButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.showSearchLocationActivity());

        viewModel.outputs.setLocationText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setLocationText);
    }

    private void setLocationText(final @NonNull String location) {
        this.locationButton.setText(location);
    }

    private void showSearchLocationActivity() {
        final Intent intent = new Intent(this, SearchLocationActivity.class);
        startActivityForResult(intent, SEARCH_LOCATION);
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }
}
