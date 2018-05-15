package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.slideInFromLeft
import com.travlog.android.apps.viewmodels.PostNoteViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_post_note.*

@RequiresActivityViewModel(PostNoteViewModel::class)
class PostNoteActivity : BaseActivity<PostNoteViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_post_note)
        setSupportActionBar(this.toolbar)
        setDisplayHomeAsUpEnabled(true)

        RxTextView.textChanges(this.title_edit)
                .map { it.toString() }
                .compose(bindToLifecycle())
                .subscribe(viewModel!!.inputs::title)

        RxView.clicks(this.add_destination)
                .compose(bindToLifecycle())
                .subscribe { this.showDestinationActivity() }

        addDisposable(
                viewModel!!.outputs.back()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { this.back() })
    }

    private fun showDestinationActivity() {
        val intent = Intent(this, DestinationActivity::class.java)
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            viewModel!!.saveClick()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
