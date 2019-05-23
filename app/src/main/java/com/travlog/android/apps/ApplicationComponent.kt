package com.travlog.android.apps

import com.travlog.android.apps.libs.ActivityViewModelModule
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.ui.activities.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ActivityViewModelModule::class])
interface ApplicationComponent {

    fun environment(): Environment

    fun inject(application: TravlogApplication)

    fun inject(activity: SplashActivity)
    fun inject(activity: LinkedAccountsActivity)
    fun inject(activity: NoteDetailsActivity)
    fun inject(activity: MyPageActivity)
    fun inject(activity: SignUpActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: EditNoteActivity)
    fun inject(activity: SearchLocationActivity)
    fun inject(activity: SettingsActivity)
    fun inject(activity: DestinationActivity)
    fun inject(activity: SignInActivity)
    fun inject(activity: SetUsernameActivity)
    fun inject(activity: PostNoteActivity)
}
