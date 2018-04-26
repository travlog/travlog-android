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
        activityResult.intent = intent;


        return activityResult;
    }

}
