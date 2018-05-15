package com.travlog.android.apps.models

import android.os.Parcel
import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Prediction() : SearchSuggestion {

    var description = ""
    var id = ""
    @SerializedName("matched_substrings")
    var matchedSubstrings: List<MatchedSubstring> = ArrayList()
    var placeId = ""
    var reference = ""
    @SerializedName("structured_formatting")
    var structuredFormatting = StructuredFormatting()
    var terms: List<Term> = ArrayList()
    var types: List<String> = ArrayList()

    constructor(parcel: Parcel) : this() {
        description = parcel.readString()
        id = parcel.readString()
        placeId = parcel.readString()
        reference = parcel.readString()
        types = parcel.createStringArrayList()
    }

    inner class MatchedSubstring : Serializable {

        var length = 0
        var offset = 0
    }

    inner class StructuredFormatting : Serializable {

        @SerializedName("main_text")
        var mainText = ""
        @SerializedName("main_text_matched_substrings")
        var mainTextMatchedSubstrings: List<MatchedSubstring> = ArrayList()
        @SerializedName("secondary_text")
        var secondaryText = ""
    }

    inner class Term : Serializable {

        var offset = 0
        var value = ""
    }

    override fun getBody(): String? {
        return structuredFormatting.mainText
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeString(placeId)
        parcel.writeString(reference)
        parcel.writeStringList(types)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Prediction> {
        override fun createFromParcel(parcel: Parcel): Prediction {
            return Prediction(parcel)
        }

        override fun newArray(size: Int): Array<Prediction?> {
            return arrayOfNulls(size)
        }
    }


}
