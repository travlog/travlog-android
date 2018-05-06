package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.glide.GlideApp;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.MyPageViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.bumptech.glide.request.RequestOptions.circleCropTransform;

@RequiresActivityViewModel(MyPageViewModel.class)
public class MyPageActivity extends BaseActivity<MyPageViewModel> {

    @BindView(R.id.settings)
    View settingsButton;
    @BindView(R.id.profile_picture)
    AppCompatImageView profilePictureImage;
    @BindView(R.id.username)
    TextView usernameText;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_my_page);

        RxView.clicks(settingsButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.startSettingsActivity());

        RxView.clicks(usernameText)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.startSetUserNameActivity());

        viewModel.outputs.setProfilePicture()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setProfilePicture);

        viewModel.outputs.setUsernameText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setUsernameText);
    }

    private void setProfilePicture(final @NonNull String profilePicture) {
        GlideApp.with(this)
                .load(profilePicture)
                .apply(circleCropTransform())
                .into(profilePictureImage);
    }

    private void setUsernameText(final @NonNull String username) {
        this.usernameText.setText(username);
    }

    private void startSettingsActivity() {
        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }

    private void startSetUserNameActivity() {
        final Intent intent = new Intent(this, SetUsernameActivity.class);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }
}
