package com.travlog.android.apps.libs;

import android.content.SharedPreferences;

import com.travlog.android.apps.services.ApiClientType;

public class Environment {

    public ApiClientType apiClient;
    public BuildKt build;
    public CurrentUserTypeKt currentUser;
    public SharedPreferences sharedPreferences;
}
