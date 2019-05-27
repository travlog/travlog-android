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

package com.travlog.android.apps

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho
import com.travlog.android.apps.libs.db.realm.RealmHelper
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class TravlogApplication : Application() {

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())

        Timber.plant(Timber.DebugTree())

        this.component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        component().inject(this)

        Crashlytics.setString("git-sha", component.environment().build.sha())
        AppEventsLogger.activateApp(this)

        RealmHelper.init(this)

        //        Stetho.initialize(
        //                Stetho.newInitializerBuilder(this)
        //                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        //                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
        //                        .build());
        Stetho.initializeWithDefaults(this)
    }

    fun component(): ApplicationComponent {
        return this.component
    }
}
