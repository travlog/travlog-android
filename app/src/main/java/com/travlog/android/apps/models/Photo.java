package com.travlog.android.apps.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    public String id;
    public String photoReference;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.photoReference);
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.id = in.readString();
        this.photoReference = in.readString();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
