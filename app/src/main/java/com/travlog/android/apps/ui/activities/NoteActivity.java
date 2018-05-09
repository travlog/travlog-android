package com.travlog.android.apps.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;

import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.viewmodels.NoteViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft;

@RequiresActivityViewModel(NoteViewModel.class)
public class NoteActivity extends BaseActivity<NoteViewModel> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    AppCompatTextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_note);
        setSupportActionBar(toolbar);
        setDisplayHomeAsUpEnabled(true);

        viewModel.outputs.setTitleText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setTitleText);
    }

    private void setTitleText(final @NonNull String title) {
        this.title.setText(title);
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}
