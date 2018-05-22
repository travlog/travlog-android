package com.travlog.android.apps.libs.utils

import android.app.Activity
import android.content.Context
import android.util.Pair
import com.travlog.android.apps.R

object TransitionUtils {

    fun transition(context: Context, transition: Pair<Int, Int>) {
        if (context !is Activity) {
            return
        }

        context.overridePendingTransition(transition.first, transition.second)
    }

    fun slideInFromRight(): Pair<Int, Int> {
        return Pair.create(R.anim.slide_in_right, R.anim.fade_out_slide_out_left)
    }

    fun slideInFromLeft(): Pair<Int, Int> {
        return Pair.create(R.anim.fade_in_slide_in_left, R.anim.slide_out_right)
    }
}
