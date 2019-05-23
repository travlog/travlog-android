/*
 * Copyright 2019 fobidlim. All rights reserved.
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

package com.travlog.android.apps.libs

import androidx.lifecycle.ViewModel
import com.travlog.android.apps.ApplicationModule
import com.travlog.android.apps.ViewModelKey
import com.travlog.android.apps.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ApplicationModule::class])
abstract class ActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LinkedAccountsViewModel::class)
    internal abstract fun linkedAccountsViewModel(viewModel: LinkedAccountsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    internal abstract fun signUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchLocationViewModel::class)
    internal abstract fun searchLocationViewModel(viewModel: SearchLocationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetUsernameViewModel::class)
    internal abstract fun setUsernameViewModel(viewModel: SetUsernameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    internal abstract fun settingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyPageViewModel::class)
    internal abstract fun myPageViewModel(viewModel: MyPageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteDetailsViewModel::class)
    internal abstract fun noteDetailsViewModel(viewModel: NoteDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditNoteViewModel::class)
    internal abstract fun editNoteViewModel(viewModel: EditNoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DestinationViewModel::class)
    internal abstract fun destinationViewModel(viewModel: DestinationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun signInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostNoteViewModel::class)
    internal abstract fun postNoteViewModel(viewModel: PostNoteViewModel): ViewModel
}