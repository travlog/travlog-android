package com.travlog.android.apps.libs.utils;

import android.support.annotation.NonNull;

import java.util.List;

public final class ListUtils {

    private ListUtils() {
    }

    /**
     * Checks the size of a list and returns `true` if the list is non empty.
     */
    public static <T> boolean nonEmpty(final @NonNull List<T> xs) {
        return xs.size() > 0;
    }
}
