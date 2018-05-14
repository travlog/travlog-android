package com.travlog.android.apps.libs.utils

import android.view.View
import java.util.function.Consumer

/**
 * Sets the visiblity of a view to {@link View#VISIBLE} or {@link View#GONE}. Setting
 * the view to GONE removes it from the layout so that it no longer takes up any space.
 */
fun setGone(view: View, gone: Boolean) {
    if (gone) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

fun setGone(view: View): Consumer<Boolean> {
    return Consumer { gone -> setGone(view, gone) }
}