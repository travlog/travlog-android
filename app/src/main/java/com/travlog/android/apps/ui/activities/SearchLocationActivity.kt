package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Pair
import android.view.WindowManager
import butterknife.BindColor
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.travlog.android.apps.R
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel
import com.travlog.android.apps.libs.rx.views.RxFloatingSearchView
import com.travlog.android.apps.libs.utils.slideInFromLeft
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.ui.IntentKey.PREDICTION
import com.travlog.android.apps.viewmodels.SearchLocationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_search_location.*
import timber.log.Timber

@RequiresActivityViewModel(SearchLocationViewModel::class)
class SearchLocationActivity : BaseActivity<SearchLocationViewModel>() {

    @BindColor(R.color.accent)
    @JvmField
    var accentColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.apply {
            attributes
                    .apply {
                        dimAmount = 0.75f
                    }
                    .let {
                        this.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        this.attributes = it
                    }
        }

        setContentView(R.layout.a_search_location)

        this.search_view.setOnHomeActionClickListener { this.back() }
        this.search_view.setSearchFocused(true)
        this.search_view.setOnBindSuggestionCallback { _, _, textView, item, _ ->
            val prediction = item as Prediction

            var text = prediction.body ?: ""

            for (mainTextMatchedSubstring in prediction.structuredFormatting.mainTextMatchedSubstrings) {
                val substring = text.substring(mainTextMatchedSubstring.offset, mainTextMatchedSubstring.length)
                text = text.replaceFirst(substring.toRegex(), "<font color=\"$accentColor\">$substring</font>")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                textView.text = Html.fromHtml(text)
            }
        }

        this.search_view.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
            override fun onFocus() {
                viewModel!!.inputs.query(search_view.query)
            }

            override fun onFocusCleared() {}
        })

        this.search_view.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
                viewModel!!.inputs.predictionClick(searchSuggestion as Prediction)
            }

            override fun onSearchAction(currentQuery: String) {
                viewModel!!.inputs.query(currentQuery)
            }
        })

        RxFloatingSearchView.queryChanges(this.search_view)
                .compose(bindToLifecycle())
                .map { it.toString() }
                .subscribe { viewModel!!.inputs.query(it) }

        viewModel!!.outputs.swapSuggestions()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this.search_view::swapSuggestions)

        viewModel!!.outputs.setResultAndBack()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setResultAndBack)
    }

    private fun setResultAndBack(prediction: Prediction) {
        Timber.d("setResultAndBack: %s", prediction.structuredFormatting.mainText)

        val intent = Intent()
        intent.putExtra(PREDICTION, prediction)
        setResult(RESULT_OK, intent)
        back()
    }

    override fun exitTransition(): Pair<Int, Int>? {
        return slideInFromLeft()
    }
}
