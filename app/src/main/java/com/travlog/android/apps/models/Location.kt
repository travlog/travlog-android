package com.travlog.android.apps.models

import android.os.Parcel
import android.os.Parcelable

class Location() : Parcelable {

    var lid = ""
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

    constructor(parcel: Parcel) : this() {
        lid = parcel.readString() ?: ""
        locality = parcel.readString() ?: ""
        administrativeAreaLevel1 = parcel.readString() ?: ""
        administrativeAreaLevel2 = parcel.readString() ?: ""
        country = parcel.readString() ?: ""
        address = parcel.readString() ?: ""
        latitude = parcel.readFloat()
        longitude = parcel.readFloat()
        name = parcel.readString() ?: ""
        placeId = parcel.readString() ?: ""
        reference = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lid)
        parcel.writeString(locality)
        parcel.writeString(administrativeAreaLevel1)
        parcel.writeString(administrativeAreaLevel2)
        parcel.writeString(country)
        parcel.writeString(address)
        parcel.writeFloat(latitude)
        parcel.writeFloat(longitude)
        parcel.writeString(name)
        parcel.writeString(placeId)
        parcel.writeString(reference)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Location(lid='$lid', locality='$locality', administrativeAreaLevel1='$administrativeAreaLevel1', administrativeAreaLevel2='$administrativeAreaLevel2', country='$country', address='$address', latitude=$latitude, longitude=$longitude, name='$name', placeId='$placeId', reference='$reference')"
    }
}