package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.ActivityRequestCodes.DESTINATION
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.viewmodels.PostNoteViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_post_note.*

@RequiresActivityViewModel(PostNoteViewModel::class)
class PostNoteActivity : BaseActivity<PostNoteViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.a_post_note).apply {
            setSupportActionBar(toolbar)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel?.apply {
            RxTextView.textChanges(title_edit)
                    .map { it.toString() }
                    .compose(bindToLifecycle())
                    .subscribe(inputs::title)

            RxView.clicks(add_destination)
                    .compose(bindToLifecycle())
                    .subscribe { showDestinationActivity() }

            addDisposable(
                    outputs.setResultAndBack()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { back() }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when {
                item.itemId == R.id.menu_save -> {
                    viewModel?.inputs?.saveClick()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun showDestinationActivity() {
        Intent(this, DestinationActivity::class.java).let {
            startActivityForResult(it, DESTINATION)
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
        }
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
