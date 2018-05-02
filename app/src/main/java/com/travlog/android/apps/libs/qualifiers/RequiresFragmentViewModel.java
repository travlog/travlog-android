package com.travlog.android.apps.libs.qualifiers;

import com.travlog.android.apps.FragmentViewModel;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresFragmentViewModel {
    Class<? extends FragmentViewModel> value();
}
