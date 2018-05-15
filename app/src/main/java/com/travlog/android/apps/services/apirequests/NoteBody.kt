package com.travlog.android.apps.services.apirequests

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class NoteBody() : Parcelable {

    var title = ""
    var memo = ""
    var createdDate = Date()
    var modifiedDate = Date()

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        memo = parcel.readString()
        createdDate = Date(parcel.readLong())
        modifiedDate = Date(parcel.readLong())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(memo)
        parcel.writeLong(createdDate.time)
        parcel.writeLong(modifiedDate.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteBody> {

        override fun createFromParcel(parcel: Parcel): NoteBody {
            return NoteBody(parcel)
        }
        override fun newArray(size: Int): Array<NoteBody?> {
            return arrayOfNulls(size)
        }

    }

    override fun toString(): String {
        return "NoteBody(title='$title', memo='$memo', createdDate=$createdDate, modifiedDate=$modifiedDate)"
    }
}
