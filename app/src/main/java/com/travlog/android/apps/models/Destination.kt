package com.travlog.android.apps.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Destination() : Parcelable {

    var id = ""
    var startDate: Date? = null
    var endDate: Date? = null
    var location: Location? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        startDate = Date(parcel.readLong())
        endDate = Date(parcel.readLong())
        location = parcel.readParcelable(Location::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(startDate?.time ?: -1L)
        parcel.writeLong(endDate?.time ?: -1L)
        parcel.writeParcelable(location, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Destination> {
        override fun createFromParcel(parcel: Parcel): Destination {
            return Destination(parcel)
        }

        override fun newArray(size: Int): Array<Destination?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Destination(id='$id', startDate=$startDate, endDate=$endDate, location=$location)"
    }
}
