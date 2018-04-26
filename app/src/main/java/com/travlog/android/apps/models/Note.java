package com.travlog.android.apps.models;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Note implements RealmModel {

    public static final String FIELD_IDX = "idx";
    public static final String FIELD_NID = "nid";
    public static final String FIELD_DESTINATIONS = "destinations";
    public static final String FIELD_PERIOD_START = "periodStart";
    public static final String FIELD_PERIOD_END = "periodEnd";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_REMOVED = "removed";
    public static final String FIELD_SYNCED = "synced";

    @PrimaryKey
    @Index
    public int idx;
    @Ignore
    public int tempIdx;
    public String nid;
    public String title;
    public RealmList<Destination> destinations;
    public long periodStart;
    public long periodEnd;
    public String memo;
    public long createdDate;
    public long modifiedDate;
    public boolean removed;
    public boolean synced;

    @Override
    public String toString() {
        return "Note{" +
                "idx=" + idx +
                ", tempIdx=" + tempIdx +
                ", nid='" + nid + '\'' +
                ", title='" + title + '\'' +
                ", destinations=" + destinations +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", memo='" + memo + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", removed=" + removed +
                ", synced=" + synced +
                '}';
    }
}
