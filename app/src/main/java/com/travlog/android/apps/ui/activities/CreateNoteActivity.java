package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Pair;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.CreateNoteViewModel;

import butterknife.BindView;

import static com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft;

@RequiresActivityViewModel(CreateNoteViewModel.class)
public class CreateNoteActivity extends BaseActivity<CreateNoteViewModel> {

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
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}
