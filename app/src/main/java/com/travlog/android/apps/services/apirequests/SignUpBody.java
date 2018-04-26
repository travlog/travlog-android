package com.travlog.android.apps.services.apirequests;

import android.os.Parcel;
import android.os.Parcelable;

public class SignUpBody implements Parcelable {

    public String email;
    public String password;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.name);
    }

    public SignUpBody() {
    }

    protected SignUpBody(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.name = in.readString();
    }

    public static final Creator<SignUpBody> CREATOR = new Creator<SignUpBody>() {
        @Override
        public SignUpBody createFromParcel(Parcel source) {
            return new SignUpBody(source);
        }

        @Override
        public SignUpBody[] newArray(int size) {
            return new SignUpBody[size];
        }
    };
}
