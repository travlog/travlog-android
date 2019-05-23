package com.travlog.android.apps.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Note : RealmObject() {

    @PrimaryKey
    var id = ""
    var title = ""
    var memo = ""
    var destinations: RealmList<Destination> = RealmList()
    var createdAt: Date = Date()

    companion object {
        const val FIELD_ID = "id"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
