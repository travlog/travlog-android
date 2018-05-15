package com.travlog.android.apps

import com.travlog.android.apps.libs.Environment

import javax.inject.Singleton

import dagger.Component

@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    fun environment(): Environment

    fun inject(application: TravlogApplication)
}
