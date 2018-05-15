package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.widget.RxTextView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.slideInFromLeft
import com.travlog.android.apps.viewmodels.EditNoteViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_post_note.*

@RequiresActivityViewModel(EditNoteViewModel::class)
class EditNoteActivity : BaseActivity<EditNoteViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_post_note)
        setSupportActionBar(this.toolbar)
        setDisplayHomeAsUpEnabled(true)

        RxTextView.textChanges(this.title_edit)
                .map<String>({ it.toString() })
                .compose(bindToLifecycle())
                .subscribe({ viewModel!!.inputs.title(it) })

        viewModel!!.outputs.setTitleText()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.setTitleText(it) })

        addDisposable(
                viewModel!!.outputs.back()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ this.back() }))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            viewModel!!.inputs.saveClick()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTitleText(title: String) {
        this.title_edit.setText(title)
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
