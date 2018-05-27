package com.travlog.android.apps.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class Note() : Parcelable {

    var nid = ""
    var title = ""
    var memo = ""
    @SerializedName("Destinations")
    var destinations: MutableList<Destination> = ArrayList()

    constructor(parcel: Parcel) : this() {
        nid = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
        memo = parcel.readString() ?: ""
        destinations = parcel.createTypedArrayList(Destination)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nid)
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
        return "Note(nid='$nid', title='$title', memo='$memo', destinations=$destinations)"
    }
}
