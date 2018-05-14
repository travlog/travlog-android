package com.travlog.android.apps.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Pair;
import android.view.Window;
import android.view.WindowManager;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.travlog.android.apps.R;
import com.travlog.android.apps.libs.BaseActivity;
import com.travlog.android.apps.libs.qualifiers.RequiresActivityViewModel;
import com.travlog.android.apps.libs.rx.views.RxFloatingSearchView;
import com.travlog.android.apps.models.Prediction;
import com.travlog.android.apps.viewmodels.SearchLocationViewModel;

import butterknife.BindColor;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.travlog.android.apps.libs.utils.TransitionUtilsKt.slideInFromLeft;
import static com.travlog.android.apps.ui.IntentKey.PREDICTION;

@RequiresActivityViewModel(SearchLocationViewModel.class)
public class SearchLocationActivity extends BaseActivity<SearchLocationViewModel> {

    @BindView(R.id.search_view)
    FloatingSearchView searchView;

    @BindColor(R.color.accent)
    int accentColor;

    @BindColor(R.color.accent)
    ColorStateList accentColorState;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();

        final WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.75f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(layoutParams);

        setContentView(R.layout.a_search_location);

        searchView.setOnHomeActionClickListener(this::back);
        searchView.setSearchFocused(true);
        searchView.setOnBindSuggestionCallback((suggestionView, leftIcon, textView, item, itemPosition) -> {
            final Prediction prediction = (Prediction) item;

            String text = prediction.getBody();

            for (Prediction.MatchedSubstring mainTextMatchedSubstring : prediction.structuredFormatting.mainTextMatchedSubstrings) {
                final String substring = text.substring(mainTextMatchedSubstring.offset, mainTextMatchedSubstring.length);
                text = text.replaceFirst(substring, "<font color=\"" + accentColor + "\">" + substring + "</font>");
            }

            textView.setText(Html.fromHtml(text));
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                viewModel.inputs.query(searchView.getQuery());
            }

            @Override
            public void onFocusCleared() {
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                viewModel.inputs.predictionClick((Prediction) searchSuggestion);
            }

            @Override
            public void onSearchAction(String currentQuery) {
                viewModel.inputs.query(currentQuery);
            }
        });

        RxFloatingSearchView.queryChanges(searchView)
                .compose(bindToLifecycle())
                .map(CharSequence::toString)
                .subscribe(viewModel.inputs::query);

        viewModel.outputs.swapSuggestions()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchView::swapSuggestions);

        viewModel.outputs.setResultAndBack()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setResultAndBack);
    }

    private void setResultAndBack(final @NonNull Prediction prediction) {
        Timber.d("setResultAndBack: %s", prediction.structuredFormatting.mainText);

        final Intent intent = new Intent();
        intent.putExtra(PREDICTION, prediction);
        setResult(RESULT_OK, intent);
        back();
    }

    @Nullable
    @Override
    protected Pair<Integer, Integer> exitTransition() {
        return slideInFromLeft();
    }
}
