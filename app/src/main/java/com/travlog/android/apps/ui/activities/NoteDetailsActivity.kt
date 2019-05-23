package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding3.view.clicks
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.ui.IntentKey.NOTE
import com.travlog.android.apps.ui.IntentKey.NOTE_ID
import com.travlog.android.apps.ui.adapters.DestinationAdapter
import com.travlog.android.apps.viewmodels.NoteDetailsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_note_details.*
import javax.inject.Inject

class NoteDetailsActivity : BaseActivity<NoteDetailsViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var destinationAdapter: DestinationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_note_details).run {
            setSupportActionBar(toolbar)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel?.apply {
            destinationAdapter = DestinationAdapter(this)
            recycler_view.adapter = destinationAdapter

            delete_button.clicks()
                    .compose(bindToLifecycle())
                    .subscribe { inputs.deleteClick() }

            outputs.setTitleText()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { setTitleText(it) }

            outputs.updateDestinations()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { destinationAdapter.clearData() }
                    .subscribe { destinationAdapter.updateData(it) }

            outputs.showEditNoteActivity()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { showEditNoteActivity(it) }

            addDisposable(
                    outputs.finish()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { back() }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.itemId.let {
            return when (it) {
                R.id.menu_edit -> {
                    viewModel?.inputs?.editClick()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setTitleText(title: String) {
        this.note_title.text = title
    }

    private fun showEditNoteActivity(note: Note) =
            Intent(this, EditNoteActivity::class.java).let {
                it.putExtra(NOTE_ID, note.id)
                startActivityWithTransition(it, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
            }

    override fun exitTransition(): Pair<Int, Int>? = slideInFromLeft()
}
