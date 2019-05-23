package com.travlog.android.apps

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho
import com.travlog.android.apps.libs.db.realm.RealmHelper
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class TravlogApplication : Application() {

    private var component: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())

        Timber.plant(Timber.DebugTree())

        this.component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        component()!!.inject(this)

        Crashlytics.setString("git-sha", component!!.environment().build.sha())
        AppEventsLogger.activateApp(this)

        RealmHelper.getInstance().init(this)

        //        Stetho.initialize(
        //                Stetho.newInitializerBuilder(this)
        //                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        //                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
        //                        .build());
        Stetho.initializeWithDefaults(this)
    }

    fun component(): ApplicationComponent? {
        return this.component
    }
}
