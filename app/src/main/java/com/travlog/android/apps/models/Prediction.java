package com.travlog.android.apps.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Prediction implements SearchSuggestion, Parcelable {

    public String description;
    public String id;
    @SerializedName("matched_substrings")
    public List<MatchedSubstrings> matchedSubstrings;
    @SerializedName("place_id")
    public String placeId;
    public String reference;
    public List<Term> terms;
    public List<String> types;

    public class MatchedSubstrings {

        public int length;
        public int offset;
    }

    public class Term {

        public int offset;
        public String value;
    }

    @Override
    public String getBody() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.placeId);
        dest.writeString(this.reference);
    }

    public Prediction() {
    }

    protected Prediction(Parcel in) {
        this.description = in.readString();
        this.placeId = in.readString();
        this.reference = in.readString();
    }

    public static final Creator<Prediction> CREATOR = new Creator<Prediction>() {
        @Override
        public Prediction createFromParcel(Parcel source) {
            return new Prediction(source);
        }

        @Override
        public Prediction[] newArray(int size) {
            return new Prediction[size];
        }
    };
}
