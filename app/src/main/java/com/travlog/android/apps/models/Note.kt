package com.travlog.android.apps.models

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

class Note() : Parcelable {

    var id = ""
    var title = ""
    var memo = ""
    var destinations: MutableList<Destination> = ArrayList()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
        memo = parcel.readString() ?: ""
        destinations = parcel.createTypedArrayList(Destination)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(memo)
        parcel.writeTypedList(destinations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Note(id='$id', title='$title', memo='$memo', destinations=$destinations)"
    }
}
