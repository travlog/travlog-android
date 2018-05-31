package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.ui.IntentKey.NOTE
import com.travlog.android.apps.ui.adapters.NoteAdapter
import com.travlog.android.apps.ui.fragments.MainMenuBottomSheetDialogFragment
import com.travlog.android.apps.viewmodels.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_main.*
import timber.log.Timber

@RequiresActivityViewModel(MainViewModel::class)
class MainActivity : BaseActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.app_bar.outlineProvider = null
        }

        viewModel?.apply {
            swipe_refresh.setOnRefreshListener(this::refresh)

            val adapter = NoteAdapter(this)
            recycler_view.adapter = adapter

            RxView.clicks(add_note)
                    .compose(bindToLifecycle())
                    .subscribe { showPostActivity() }

            RxView.clicks(menu)
                    .compose(bindToLifecycle())
                    .subscribe { showMainMenuBottomSheet() }

            RxView.clicks(overflow)
                    .compose(bindToLifecycle())
                    .subscribe()

            outputs.setRefreshing()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { setRefreshing(it) }

            outputs.clearNotes()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapter.clearData() }

            outputs.updateData()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapter.updateData(it) }

            outputs.showNoteDetailsActivity()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { showNoteDetailsActivity(it) }

            outputs.updateNotes()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapter.updateNote(it) }

            outputs.deleteNote()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapter.deleteData(it) }

            this.loadMore()
        }
    }

    private fun setRefreshing(refreshing: Boolean) {
        swipe_refresh.isRefreshing = refreshing
    }

    private fun showPostActivity() {
        Intent(this, PostNoteActivity::class.java).let {
            startActivityWithTransition(it, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
        }
    }

    private fun showMainMenuBottomSheet() {
        MainMenuBottomSheetDialogFragment.newInstance()
                .show(supportFragmentManager, "MainMenuBottomSheetDialogFragment")
    }

    private fun showNoteDetailsActivity(note: Note) {
        Intent(this, NoteDetailsActivity::class.java).let {
            it.putExtra(NOTE, note)
            startActivityWithTransition(it, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
        }
    }
}
