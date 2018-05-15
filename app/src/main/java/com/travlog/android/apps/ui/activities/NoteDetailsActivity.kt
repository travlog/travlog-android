package com.travlog.android.apps.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.Menu
import android.view.MenuItem

import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.viewmodels.NoteDetailsViewModel

import io.reactivex.android.schedulers.AndroidSchedulers

import com.travlog.android.apps.libs.utils.slideInFromLeft
import com.travlog.android.apps.ui.IntentKey

import kotlinx.android.synthetic.main.a_note_details.*

@RequiresActivityViewModel(NoteDetailsViewModel::class)
class NoteDetailsActivity : BaseActivity<NoteDetailsViewModel>() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_note_details)
        setSupportActionBar(this.toolbar)
        setDisplayHomeAsUpEnabled(true)

        viewModel!!.outputs.setTitleText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { this.setTitleText(it) }

        viewModel!!.outputs.showEditNoteActivity()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.showEditNoteActivity(it) })

        addDisposable(
                viewModel!!.outputs.back()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ this.back() }))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.menu_edit -> {
                viewModel!!.inputs.editClick()
                return true
            }
            R.id.menu_delete -> {
                viewModel!!.inputs.deleteClick()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setTitleText(title: String) {
        this.note_title.text = title
    }

    private fun showEditNoteActivity(note: Note) {
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra(IntentKey.NOTE, note)
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
