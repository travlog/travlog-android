package com.travlog.android.apps;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.travlog.android.apps.libs.ApiEndpoint;
import com.travlog.android.apps.libs.Build;
import com.travlog.android.apps.libs.CurrentUser;
import com.travlog.android.apps.libs.CurrentUserType;
import com.travlog.android.apps.libs.Environment;
import com.travlog.android.apps.libs.perferences.StringPreference;
import com.travlog.android.apps.libs.perferences.StringPreferenceType;
import com.travlog.android.apps.libs.qualifiers.AccessTokenPreference;
import com.travlog.android.apps.libs.qualifiers.ApiEndpointPreference;
import com.travlog.android.apps.libs.qualifiers.ApplicationContext;
import com.travlog.android.apps.libs.qualifiers.PackageNameString;
import com.travlog.android.apps.libs.qualifiers.UserPreference;
import com.travlog.android.apps.services.ApiClient;
import com.travlog.android.apps.services.ApiClientType;
import com.travlog.android.apps.services.ApiService;
import com.travlog.android.apps.services.interceptors.ApiRequestInterceptor;
import com.travlog.android.apps.ui.SharedPreferenceKey;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(final @NonNull Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    static Environment provideEnvironment(final @NonNull ApiClientType apiClient,
                                          final @NonNull Build build,
                                          final @NonNull CurrentUserType currentUser,
                                          final @NonNull SharedPreferences sharedPreferences) {

        final Environment environment = new Environment();
        environment.apiClient = apiClient;
        environment.build = build;
        environment.currentUser = currentUser;
        environment.sharedPreferences = sharedPreferences;

        return environment;
    }

    @Provides
    @Singleton
    @NonNull
    static ApiClientType provideApiClientType(final @NonNull ApiService apiService) {
        return new ApiClient(apiService);
    }

    @Provides
    @Singleton
    @NonNull
    static OkHttpClient provideOkHttpClient(final @NonNull ApiRequestInterceptor apiRequestInterceptor,
                                            final @NonNull HttpLoggingInterceptor httpLoggingInterceptor) {

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // Only log in debug mode to avoid leaking sensitive information.
//        if (build.isDebug()) {
        builder.addInterceptor(httpLoggingInterceptor);
//        }

        return builder
                .addInterceptor(apiRequestInterceptor)
                .build();
    }

    @Provides
    @Singleton
    ApiEndpoint provideApiEndpoint(@ApiEndpointPreference final @NonNull StringPreferenceType apiEndpointPreference) {
        return ApiEndpoint.from(apiEndpointPreference.get());
    }

    @Provides
    @Singleton
    @ApiEndpointPreference
    @NonNull
    StringPreferenceType provideApiEndpointPreference(final @NonNull SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, "debug_api_endpoint", ApiEndpoint.LOCAL.url());
    }

    @Provides
    @Singleton
    @NonNull
    static Retrofit provideApiRetrofit(final @NonNull ApiEndpoint apiEndpoint,
                                       final @NonNull OkHttpClient okHttpClient) {
        return createRetrofit(apiEndpoint.url(), okHttpClient);
    }

    @Provides
    @Singleton
    @NonNull
    static ApiRequestInterceptor provideApiRequestInterceptor(final @NonNull CurrentUserType currentUser,
                                                              final @NonNull ApiEndpoint endpoint) {

        return new ApiRequestInterceptor(currentUser, endpoint.url());
    }

    @Provides
    @Singleton
    @NonNull
    static ApiService provideApiService(final @NonNull Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }


    @Provides
    @Singleton
    @NonNull
    static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private static @NonNull
    Retrofit createRetrofit(final @NonNull String baseUrl,
                            final @NonNull OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
//                .client(SocketUtils.enableTLS1_2OnPreLollipop(okHttpClient))
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @AccessTokenPreference
    @NonNull
    static StringPreferenceType provideAccessTokenPreference(final @NonNull SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, SharedPreferenceKey.ACCESS_TOKEN);
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.application;
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    AssetManager provideAssetManager() {
        return this.application.getAssets();
    }

    @Provides
    @Singleton
    @NonNull
    static Build provideBuild(final @NonNull PackageInfo packageInfo) {
        return new Build(packageInfo);
    }

    @Provides
    @Singleton
    static CurrentUserType provideCurrentUser(final @AccessTokenPreference @NonNull StringPreferenceType accessTokenPreference,
                                              final @NonNull @UserPreference StringPreferenceType userPreference) {

        return new CurrentUser(accessTokenPreference, userPreference);
    }

    @Provides
    @Singleton
    static PackageInfo providePackageInfo(final @NonNull Application application) {
        try {
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Provides
    @Singleton
    @PackageNameString
    static String providePackageName(final @NonNull Application application) {
        return application.getPackageName();
    }

    @Provides
    @Singleton
    static Resources provideResources(final @ApplicationContext @NonNull Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this.application);
    }

    @Provides
    @Singleton
    @UserPreference
    @NonNull
    static StringPreferenceType provideUserPreference(final @NonNull SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, SharedPreferenceKey.USER);
    }
}
