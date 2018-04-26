package com.travlog.android.apps.libs.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;

public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmail(final @NonNull CharSequence str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public static boolean isEmpty(final @Nullable String str) {
        return str == null || str.trim().length() == 0;
    }
}
