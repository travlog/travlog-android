package com.travlog.android.apps.services.apirequests

import android.os.Parcel
import android.os.Parcelable

class SignUpBody() : Parcelable {

    var email = ""
    var password = ""

    constructor(parcel: Parcel) : this() {
        email = parcel.readString()
        password = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SignUpBody> {
        override fun createFromParcel(parcel: Parcel): SignUpBody {
            return SignUpBody(parcel)
        }

        override fun newArray(size: Int): Array<SignUpBody?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "SignUpBody(email='$email', password='$password')"
    }
}
