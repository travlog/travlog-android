package com.travlog.android.apps

import com.travlog.android.apps.libs.ActivityViewModelModule
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.ui.activities.MainActivity
import com.travlog.android.apps.ui.activities.SignInActivity
import com.travlog.android.apps.ui.activities.SplashActivity

import javax.inject.Singleton

import dagger.Component

@Singleton
@Component(modules = [ApplicationModule::class, ActivityViewModelModule::class])
interface ApplicationComponent {

    fun environment(): Environment

    fun inject(application: TravlogApplication)

    fun inject(activity: SplashActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: SignInActivity)
}
