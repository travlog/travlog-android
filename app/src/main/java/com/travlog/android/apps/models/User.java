package com.travlog.android.apps.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String uid;
    public String name;
    public String username;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.name);
        dest.writeString(this.username);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.uid = in.readString();
        this.name = in.readString();
        this.username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", username'" + username + '\'' +
                '}';
    }
}
