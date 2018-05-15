package com.travlog.android.apps.models

import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable {

    var userId = ""
    var name = ""
    var username = ""
    var profilePicture = ""

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        name = parcel.readString()
        username = parcel.readString()
        profilePicture = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(username)
        parcel.writeString(profilePicture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
