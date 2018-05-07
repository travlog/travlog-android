package com.travlog.android.apps.ui.data;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ActivityResult {

    public int requestCode;
    public int resultCode;
    public Intent intent;

    public static @NonNull
    ActivityResult create(final int requestCode, final int resultCode, final @Nullable Intent intent) {
        final ActivityResult activityResult = new ActivityResult();
        activityResult.requestCode = requestCode;
        activityResult.resultCode = resultCode;
        if (intent == null) {
            activityResult.intent = new Intent();
        } else {
            activityResult.intent = intent;
        }

        return activityResult;
    }

    @Override
    public String toString() {
        return "ActivityResult{" +
                "requestCode=" + requestCode +
                ", resultCode=" + resultCode +
                ", intent=" + intent +
                '}';
    }
}
