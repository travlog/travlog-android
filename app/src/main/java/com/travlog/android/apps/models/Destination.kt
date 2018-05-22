package com.travlog.android.apps.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Destination() : Parcelable {

    var placeId: String = ""
    var text: String = ""
    var startDate: Date? = null
    var endDate: Date? = null

    constructor(parcel: Parcel) : this() {
        placeId = parcel.readString()
        text = parcel.readString()
        startDate = Date(parcel.readLong())
        endDate = Date(parcel.readLong())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(placeId)
        parcel.writeString(text)
        parcel.writeLong(startDate?.time ?: -1)
        parcel.writeLong(endDate?.time ?: -1)
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
        return "Destination(placeId='$placeId', text='$text', startDate=$startDate, endDate=$endDate)"
    }
}
