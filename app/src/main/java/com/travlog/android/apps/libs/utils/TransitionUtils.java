package com.travlog.android.apps.libs.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.travlog.android.apps.R;

public final class TransitionUtils {

    private TransitionUtils() {
    }

    /**
     * Explicitly set a transition after starting an activity.
     *
     * @param context    The activity that started the new intent.
     * @param transition A pair of animation ids, first is the enter animation, second is the exit animation.
     */
    public static void transition(final @NonNull Context context, final @NonNull Pair<Integer, Integer> transition) {
        if (!(context instanceof Activity)) {
            return;
        }

        final Activity activity = (Activity) context;
        activity.overridePendingTransition(transition.first, transition.second);
    }

    public static @NonNull
    Pair<Integer, Integer> slideInFromRight() {
        return Pair.create(R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
    }

    public static @NonNull
    Pair<Integer, Integer> slideInFromLeft() {
        return Pair.create(R.anim.fade_in_slide_in_left, R.anim.slide_out_right);
    }
}
