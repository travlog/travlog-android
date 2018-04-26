package com.travlog.android.apps.services.apirequests;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteBody implements Parcelable {

    public String title;
    public String memo;
    public long createdDate;
    public long modifiedDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.memo);
        dest.writeLong(this.createdDate);
        dest.writeLong(this.modifiedDate);
    }

    public NoteBody() {
    }

    protected NoteBody(Parcel in) {
        this.title = in.readString();
        this.memo = in.readString();
        this.createdDate = in.readLong();
        this.modifiedDate = in.readLong();
    }

    public static final Creator<NoteBody> CREATOR = new Creator<NoteBody>() {
        @Override
        public NoteBody createFromParcel(Parcel source) {
            return new NoteBody(source);
        }

        @Override
        public NoteBody[] newArray(int size) {
            return new NoteBody[size];
        }
    };
}
