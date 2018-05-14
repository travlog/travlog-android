package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.PostNoteViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.travlog.android.apps.libs.utils.TransitionUtilsKt.slideInFromLeft;

@RequiresActivityViewModel(PostNoteViewModel.class)
public class PostNoteActivity extends BaseActivity<PostNoteViewModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title_edit)
    TextInputEditText titleEdit;
    @BindView(R.id.destination_container)
    LinearLayout destinationContainer;
    @BindView(R.id.add_destination)
    View addDestinationButton;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_post_note);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        RxTextView.textChanges(titleEdit)
                .map(CharSequence::toString)
                .compose(bindToLifecycle())
                .subscribe(viewModel.inputs::title);

        RxView.clicks(addDestinationButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.showDestinationActivity());

        viewModel.outputs.back()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::back);
    }

    private void showDestinationActivity() {
        final Intent intent = new Intent(this, DestinationActivity.class);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            viewModel.saveClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}
