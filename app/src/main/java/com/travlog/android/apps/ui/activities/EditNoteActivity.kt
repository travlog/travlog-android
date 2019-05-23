package com.travlog.android.apps.ui.activities

import android.os.Bundle
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding3.widget.textChanges
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.EditNoteViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_post_note.*

class EditNoteActivity : BaseActivity<EditNoteViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_post_note)
        setSupportActionBar(this.toolbar)
        setDisplayHomeAsUpEnabled(true)

        title_edit.textChanges()
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
        menuInflater.inflate(R.menu.menu_save, menu)
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
