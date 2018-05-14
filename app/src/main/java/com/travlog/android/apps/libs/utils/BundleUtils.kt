package com.travlog.android.apps.libs.utils

import android.os.Bundle

fun maybeGetBundle(state: Bundle?, key: String): Bundle? {
    return state?.getBundle(key)
}