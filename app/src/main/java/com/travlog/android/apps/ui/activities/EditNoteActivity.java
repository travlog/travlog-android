package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.EditNoteViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft;

@RequiresActivityViewModel(EditNoteViewModel.class)
public class EditNoteActivity extends BaseActivity<EditNoteViewModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title_edit)
    TextInputEditText titleEdit;

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

        viewModel.outputs.setTitleText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setTitleText);

        addDisposable(
                viewModel.outputs.back()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::back));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            viewModel.inputs.saveClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTitleText(final @NonNull String title) {
        this.titleEdit.setText(title);
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}