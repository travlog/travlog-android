/*
 * Copyright 2019 Travlog. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travlog.android.apps.models

import android.os.Parcel
import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Prediction() : SearchSuggestion, Serializable {

    var description = ""
    var id = ""
    @SerializedName("matched_substrings")
    var matchedSubstrings: List<MatchedSubstring> = ArrayList()
    @SerializedName("place_id")
    var placeId = ""
    var reference = ""
    @SerializedName("structured_formatting")
    var structuredFormatting = StructuredFormatting()
    var terms: List<Term> = ArrayList()
    var types: List<String> = ArrayList()

    constructor(parcel: Parcel) : this() {
        description = parcel.readString()
        id = parcel.readString()
        matchedSubstrings = parcel.createTypedArrayList(MatchedSubstring)
        placeId = parcel.readString()
        reference = parcel.readString()
        structuredFormatting = parcel.readParcelable(StructuredFormatting::class.java.classLoader)
        terms = parcel.createTypedArrayList(Term)
        types = parcel.createStringArrayList()
    }

    class MatchedSubstring() : Parcelable {

        var length = 0
        var offset = 0

        constructor(parcel: Parcel) : this() {
            length = parcel.readInt()
            offset = parcel.readInt()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(length)
            parcel.writeInt(offset)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MatchedSubstring> {
            override fun createFromParcel(parcel: Parcel): MatchedSubstring {
                return MatchedSubstring(parcel)
            }

            override fun newArray(size: Int): Array<MatchedSubstring?> {
                return arrayOfNulls(size)
            }
        }
    }

    class StructuredFormatting() : Parcelable {

        @SerializedName("main_text")
        var mainText = ""
        @SerializedName("main_text_matched_substrings")
        var mainTextMatchedSubstrings: List<MatchedSubstring> = ArrayList()
        @SerializedName("secondary_text")
        var secondaryText = ""

        constructor(parcel: Parcel) : this() {
            mainText = parcel.readString()
            secondaryText = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(mainText)
            parcel.writeString(secondaryText)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<StructuredFormatting> {
            override fun createFromParcel(parcel: Parcel): StructuredFormatting {
                return StructuredFormatting(parcel)
            }

            override fun newArray(size: Int): Array<StructuredFormatting?> {
                return arrayOfNulls(size)
            }
        }
    }

    class Term() : Parcelable {

        var offset = 0
        var value = ""

        constructor(parcel: Parcel) : this() {
            offset = parcel.readInt()
            value = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(offset)
            parcel.writeString(value)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Term> {
            override fun createFromParcel(parcel: Parcel): Term {
                return Term(parcel)
            }

            override fun newArray(size: Int): Array<Term?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun getBody(): String? {
        return structuredFormatting.mainText
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeTypedList(matchedSubstrings)
        parcel.writeString(placeId)
        parcel.writeString(reference)
        parcel.writeParcelable(structuredFormatting, flags)
        parcel.writeTypedList(terms)
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
