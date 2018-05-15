package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.ui.IntentKey
import com.travlog.android.apps.ui.adapters.NoteAdapter
import com.travlog.android.apps.ui.fragments.MainMenuBottomSheetDialogFragment
import com.travlog.android.apps.viewmodels.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_main.*

@RequiresActivityViewModel(MainViewModel::class)
class MainActivity : BaseActivity<MainViewModel>() {

    private var adapter: NoteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.app_bar.outlineProvider = null
        }

        adapter = NoteAdapter(viewModel!!)
        this.recycler_view.adapter = adapter

        RxView.clicks(this.add_note)
                .compose(bindToLifecycle())
                .subscribe { this.showPostActivity() }

        RxView.clicks(this.menu)
                .compose(bindToLifecycle())
                .subscribe { this.showMainMenuBottomSheet() }

        RxView.clicks(this.overflow)
                .compose(bindToLifecycle())
                .subscribe()

        viewModel!!.outputs.updateData()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ adapter!!.updateData(it) })

        viewModel!!.outputs.showNoteDetailsActivity()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.showNoteDetailsActivity(it) })

        viewModel!!.outputs.updateNote()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ adapter!!.updateData(it) })

        viewModel!!.outputs.deleteNote()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ adapter!!.deleteData(it) })
    }

    private fun showPostActivity() {
        val intent = Intent(this, PostNoteActivity::class.java)
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }

    private fun showMainMenuBottomSheet() {
        MainMenuBottomSheetDialogFragment.newInstance()
                .show(supportFragmentManager, "MainMenuBottomSheetDialogFragment")
    }

    private fun showNoteDetailsActivity(note: Note) {
        val intent = Intent(this, NoteDetailsActivity::class.java)
        intent.putExtra(IntentKey.NOTE, note)
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }
}
