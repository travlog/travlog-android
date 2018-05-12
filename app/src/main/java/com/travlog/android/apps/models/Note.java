package com.travlog.android.apps.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    public int id;
    public String title;
    public String memo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.memo);
    }

    public Note() {
    }

    protected Note(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.memo = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                "title='" + title + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
