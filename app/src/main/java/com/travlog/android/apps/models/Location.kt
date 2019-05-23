package com.travlog.android.apps.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Location : RealmObject() {

    @PrimaryKey
    var id = ""
    var locality = ""
    var administrativeAreaLevel1 = ""
    var administrativeAreaLevel2 = ""
    var country = ""
    var address = ""
    var latitude = 0f
    var longitude = 0f
    var name = ""
    var placeId = ""
    var reference = ""
}