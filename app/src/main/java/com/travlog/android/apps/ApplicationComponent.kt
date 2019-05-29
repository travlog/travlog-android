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
    fun inject(activity: PostDestinationActivity)
    fun inject(activity: SignInActivity)
    fun inject(activity: SetUsernameActivity)
    fun inject(activity: PostNoteActivity)
    fun inject(activity: PostPlaceActivity)
    fun inject(activity: DestinationDetailsActivity)
}
