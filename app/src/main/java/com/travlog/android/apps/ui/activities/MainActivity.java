package com.travlog.android.apps.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView;
import com.jakewharton.rxbinding2.view.RxView;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.models.Note;
import com.travlog.android.apps.ui.adapters.NoteAdapter;
import com.travlog.android.apps.viewmodels.MainViewModel;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.travlog.android.apps.ui.IntentKey.NOTE;

@RequiresActivityViewModel(MainViewModel.class)
public class MainActivity extends BaseActivity<MainViewModel> {

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.add_note)
    View addNoteButton;

    private NoteAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);

        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        adapter = new NoteAdapter(viewModel);
        recyclerView.setAdapter(adapter);

        RxView.clicks(addNoteButton)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, PostNoteActivity.class)));

        RxNavigationView.itemSelections(navigationView)
                .map(MenuItem::getItemId)
                .filter(id -> id == R.id.menu_sign_in)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, SignInActivity.class)));

        RxNavigationView.itemSelections(navigationView)
                .map(MenuItem::getItemId)
                .filter(id -> id == R.id.menu_sign_up)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, SignUpActivity.class)));

        RxNavigationView.itemSelections(navigationView)
                .map(MenuItem::getItemId)
                .filter(id -> id == R.id.menu_my_page)
                .compose(bindToLifecycle())
                .subscribe(__ -> startActivity(new Intent(this, MyPageActivity.class)));

        viewModel.outputs.updateData()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::updateData);

        viewModel.outputs.showNoteActivity()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showNoteActivity);
    }

    private void showNoteActivity(final @NonNull Note note) {
        final Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(NOTE, note);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }

    @Override
    public void back() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.back();
        }
    }
}
