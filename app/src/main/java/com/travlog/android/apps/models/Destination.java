package com.travlog.android.apps.models;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class Destination implements RealmModel, Serializable {

    public static final String FIELD_IDX = "idx";
    public static final String FIELD_DID = "did";
    public static final String FIELD_PHOTO_URL = "photoUrl";
    public static final String FIELD_REFERENCE = "photoReference";

    @PrimaryKey
    @Index
    public int idx;
    public String did;
    @Required
    public String name;
    public String photoUrl;
    public double latitude;
    public double longitude;
    public long periodStart;
    public long periodEnd;
    public String placeId;
    public String photoReference;

    @Override
    public String toString() {
        return "Destination{" +
                "idx=" + idx +
                ", did='" + did + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", placeId='" + placeId + '\'' +
                ", photoReference='" + photoReference + '\'' +
                '}';
    }
}
