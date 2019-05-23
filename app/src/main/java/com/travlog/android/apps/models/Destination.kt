package com.travlog.android.apps.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Destination : RealmObject() {

    @PrimaryKey
    var id = ""
    var startDate: Date? = null
    var endDate: Date? = null
    var location: Location? = null

    companion object {
        const val FIELD_ID = "id"
    }
}
