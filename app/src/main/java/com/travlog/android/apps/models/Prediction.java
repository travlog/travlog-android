package com.travlog.android.apps.models;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Prediction implements SearchSuggestion {

    public String description;
    public String id;
    @SerializedName("matched_substrings")
    public List<MatchedSubstring> matchedSubstrings;
    public String placeId;
    public String reference;
    @SerializedName("structured_formatting")
    public StructuredFormatting structuredFormatting;
    public List<Term> terms;
    public List<String> types;

    public class MatchedSubstring implements Serializable {

        public int length;
        public int offset;
    }

    public class StructuredFormatting implements Serializable {

        @SerializedName("main_text")
        public String mainText;
        @SerializedName("main_text_matched_substrings")
        public List<MatchedSubstring> mainTextMatchedSubstrings;
        @SerializedName("secondary_text")
        public String secondaryText;
    }

    public class Term implements Serializable {

        public int offset;
        public String value;
    }

    @Override
    public String getBody() {
        return structuredFormatting.mainText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.id);
        dest.writeList(this.matchedSubstrings);
        dest.writeString(this.placeId);
        dest.writeString(this.reference);
        dest.writeSerializable(this.structuredFormatting);
        dest.writeList(this.terms);
        dest.writeStringList(this.types);
    }

    public Prediction() {
    }

    protected Prediction(Parcel in) {
        this.description = in.readString();
        this.id = in.readString();
        this.matchedSubstrings = new ArrayList<MatchedSubstring>();
        in.readList(this.matchedSubstrings, MatchedSubstring.class.getClassLoader());
        this.placeId = in.readString();
        this.reference = in.readString();
        this.structuredFormatting = (StructuredFormatting) in.readSerializable();
        this.terms = new ArrayList<Term>();
        in.readList(this.terms, Term.class.getClassLoader());
        this.types = in.createStringArrayList();
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
