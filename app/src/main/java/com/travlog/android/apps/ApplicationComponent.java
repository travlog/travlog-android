package com.travlog.android.apps;

import com.travlog.android.apps.libs.Environment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    Environment environment();

    void inject(TravlogApplication __);
}
