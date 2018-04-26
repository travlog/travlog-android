package com.travlog.android.apps.services.apirequests;

import android.os.Parcel;
import android.os.Parcelable;

public class XauthBody implements Parcelable {

    public String email;
    public String password;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    public XauthBody() {
    }

    protected XauthBody(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
    }

    public static final Parcelable.Creator<XauthBody> CREATOR = new Parcelable.Creator<XauthBody>() {
        @Override
        public XauthBody createFromParcel(Parcel source) {
            return new XauthBody(source);
        }

        @Override
        public XauthBody[] newArray(int size) {
            return new XauthBody[size];
        }
    };
}
