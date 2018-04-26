package com.travlog.android.apps.libs;

import android.support.annotation.NonNull;
import android.view.View;

import rx.functions.Action1;

public final class ViewUtils {

    private ViewUtils() {
    }

    /**
     * Sets the visiblity of a view to {@link View#VISIBLE} or {@link View#GONE}. Setting
     * the view to GONE removes it from the layout so that it no longer takes up any space.
     */
    public static void setGone(final @NonNull View view, final boolean gone) {
        if (gone) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static Action1<Boolean> setGone(final @NonNull View view) {
        return (gone) -> setGone(view, gone);
    }
}
