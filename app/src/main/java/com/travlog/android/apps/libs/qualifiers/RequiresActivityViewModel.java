package com.travlog.android.apps.libs.qualifiers;

import com.travlog.android.apps.libs.ActivityViewModel;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresActivityViewModel {
    Class<? extends ActivityViewModel> value();
}
