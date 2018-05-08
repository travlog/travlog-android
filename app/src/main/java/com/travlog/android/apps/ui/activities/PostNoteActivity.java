package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.services.PostNoteService;
import com.travlog.android.apps.viewmodels.PostNoteViewModel;

import butterknife.BindView;

import static com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft;
import static com.travlog.android.apps.ui.IntentKey.NOTE;

@RequiresActivityViewModel(PostNoteViewModel.class)
public class PostNoteActivity extends BaseActivity<PostNoteViewModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title_edit)
    TextInputEditText titleEdit;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_create_note);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        RxTextView.textChanges(titleEdit)
                .map(CharSequence::toString)
                .compose(bindToLifecycle())
                .subscribe(viewModel.inputs::title);

        viewModel.outputs.startPostNoteService()
                .compose(bindToLifecycle())
                .subscribe(this::startPostNoteService);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            viewModel.back();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        viewModel.back();
        super.onBackPressed();
    }

    private void startPostNoteService(final @NonNull Note note) {
        final Intent intent = new Intent(this, PostNoteService.class);
        intent.putExtra(NOTE, note);
        startService(intent);
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}
