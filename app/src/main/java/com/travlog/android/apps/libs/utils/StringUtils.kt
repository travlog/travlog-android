package com.travlog.android.apps.libs.utils

import android.util.Patterns

object StringUtils {

    fun isEmail(str: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    fun isEmpty(str: String): Boolean {
        return str.trimIndent().length == 0
    }
}
