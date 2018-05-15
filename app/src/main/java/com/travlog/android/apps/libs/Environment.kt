package com.travlog.android.apps.libs

import android.content.SharedPreferences
import com.travlog.android.apps.services.ApiClientType

data class Environment(var apiClient: ApiClientType, var build: Build, var currentUser: CurrentUserType, var sharedPreferences: SharedPreferences)