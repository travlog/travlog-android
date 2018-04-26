package com.travlog.android.apps.services.apiresponses;

import android.os.Parcel;
import android.os.Parcelable;

import com.travlog.android.apps.models.User;

public class AccessTokenEnvelope implements Parcelable{

    public String accessToken;
    public User user;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeParcelable(this.user, flags);
    }

    public AccessTokenEnvelope() {
    }

    protected AccessTokenEnvelope(Parcel in) {
        this.accessToken = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<AccessTokenEnvelope> CREATOR = new Creator<AccessTokenEnvelope>() {
        @Override
        public AccessTokenEnvelope createFromParcel(Parcel source) {
            return new AccessTokenEnvelope(source);
        }

        @Override
        public AccessTokenEnvelope[] newArray(int size) {
            return new AccessTokenEnvelope[size];
        }
    };
}
