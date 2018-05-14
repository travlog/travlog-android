package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.viewmodels.NoteDetailsViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.travlog.android.apps.libs.utils.TransitionUtilsKt.slideInFromLeft;
import static com.travlog.android.apps.ui.IntentKey.NOTE;

@RequiresActivityViewModel(NoteDetailsViewModel.class)
public class NoteDetailsActivity extends BaseActivity<NoteDetailsViewModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    AppCompatTextView title;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_note_details);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        viewModel.outputs.setTitleText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setTitleText);

        viewModel.outputs.showEditNoteActivity()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showEditNoteActivity);

        addDisposable(
                viewModel.outputs.back()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::back));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.menu_edit:
                viewModel.inputs.editClick();
                return true;
            case R.id.menu_delete:
                viewModel.inputs.deleteClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setTitleText(final @NonNull String title) {
        this.title.setText(title);
    }

    private void showEditNoteActivity(final @NonNull Note note) {
        final Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(NOTE, note);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}
