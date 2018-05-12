package com.travlog.android.apps.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.ui.adapters.NoteAdapter;
import com.travlog.android.apps.ui.fragments.MainMenuBottomSheetDialogFragment;
import com.travlog.android.apps.viewmodels.MainViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.travlog.android.apps.ui.IntentKey.NOTE;

@RequiresActivityViewModel(MainViewModel.class)
public class MainActivity extends BaseActivity<MainViewModel> {

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.bottom_app_bar)
    BottomAppBar bottomAppBar;
    @BindView(R.id.menu)
    View menuButton;
    @BindView(R.id.overflow)
    View overflowButton;
    @BindView(R.id.add_note)
    View addNoteButton;

    private NoteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);

        appBarLayout.setOutlineProvider(null);

        adapter = new NoteAdapter(viewModel);
        recyclerView.setAdapter(adapter);

        RxView.clicks(addNoteButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.showPostActivity());

        RxView.clicks(menuButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> this.showMainMenuBottomSheet());

        RxView.clicks(overflowButton)
                .compose(bindToLifecycle())
                .subscribe();

        viewModel.outputs.updateData()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::updateData);

        viewModel.outputs.showNoteActivity()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showNoteActivity);

        viewModel.outputs.deleteNote()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::deleteData);
    }

    private void showPostActivity() {
        final Intent intent = new Intent(this, PostNoteActivity.class);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }

    private void showMainMenuBottomSheet() {
        MainMenuBottomSheetDialogFragment.newInstance()
                .show(getSupportFragmentManager(), "MainMenuBottomSheetDialogFragment");
    }

    private void showNoteActivity(final @NonNull Note note) {
        final Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(NOTE, note);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }
}
