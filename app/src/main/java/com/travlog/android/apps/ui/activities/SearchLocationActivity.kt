package com.travlog.android.apps.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.util.Pair
import android.view.WindowManager
import butterknife.BindColor
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.travlog.android.apps.R
import com.travlog.android.apps.ViewModelFactory
import com.travlog.android.apps.getAppInjector
import com.travlog.android.apps.getViewModel
import com.travlog.android.apps.libs.BaseActivity
import com.travlog.android.apps.libs.rx.views.RxFloatingSearchView
import com.travlog.android.apps.libs.utils.TransitionUtils.slideInFromLeft
import com.travlog.android.apps.models.Prediction
import com.travlog.android.apps.ui.IntentKey.PREDICTION
import com.travlog.android.apps.viewmodels.SearchLocationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.a_search_location.*
import javax.inject.Inject

class SearchLocationActivity : BaseActivity<SearchLocationViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @BindColor(R.color.accent)
    @JvmField
    var accentColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        getAppInjector().inject(this)
        viewModel = getViewModel(viewModelFactory)

        super.onCreate(savedInstanceState)

        window.apply {
            attributes.apply {
                dimAmount = 0.75f
                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                attributes = this
            }
        }

        setContentView(R.layout.a_search_location)

        viewModel?.apply {
            search_view.apply {
                setOnHomeActionClickListener { back() }
                setSearchFocused(true)
                setOnBindSuggestionCallback { _, _, textView, item, _ ->

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
                setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
                    override fun onFocus() {
                        inputs.query(search_view.query)
                    }

                    override fun onFocusCleared() {
                    }
                })
                setOnSearchListener(object : FloatingSearchView.OnSearchListener {
                    override fun onSearchAction(currentQuery: String?) {
                        inputs.query(currentQuery ?: "")
                    }

                    override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                        inputs.predictionClick(searchSuggestion as Prediction)
                    }

                })
            }

            RxFloatingSearchView.queryChanges(search_view)
                    .compose(bindToLifecycle())
                    .map { it.toString() }
                    .subscribe { inputs.query(it) }

            outputs.swapSuggestions()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { swapSuggestions(it) }

            outputs.setResultAndBack()
                    .compose(bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { setResultAndBack(it) }
        }
    }

    private fun swapSuggestions(suggestions: List<Prediction>) = search_view.swapSuggestions(suggestions)

    private fun setResultAndBack(prediction: Prediction) =
            Intent().apply {
                putExtra(PREDICTION, prediction as Parcelable)
                setResult(RESULT_OK, this)
                back()
            }

    override fun exitTransition(): Pair<Int, Int>? = slideInFromLeft()
}
