package com.travlog.android.apps.libs;

import android.content.SharedPreferences;

import com.travlog.android.apps.services.ApiClientType;

public class Environment {

    public ApiClientType apiClient;
    public Build build;
    public CurrentUserType currentUser;
    public SharedPreferences sharedPreferences;
}
