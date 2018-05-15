package com.travlog.android.apps.libs.perferences

interface StringPreferenceType {

    /**
     * Returns whether a value has been explicitly set for the preference.
     */
    fun isSet(): Boolean

    /**
     * Get the current value of the preference.
     */
    fun get(): String?

    /**
     * Set the preference to a value.
     */
    fun set(value: String)

    /**
     * Delete the currently stored preference.
     */
    fun delete()
}
