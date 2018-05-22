package com.travlog.android.apps.libs.utils

import android.os.Bundle

object BundleUtils {

    fun maybeGetBundle(state: Bundle?, key: String): Bundle? {
        return state?.getBundle(key)
    }
}