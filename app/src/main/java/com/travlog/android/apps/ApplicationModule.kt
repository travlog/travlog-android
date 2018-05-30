package com.travlog.android.apps

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.preference.PreferenceManager
import com.travlog.android.apps.libs.*
import com.travlog.android.apps.libs.perferences.StringPreference
import com.travlog.android.apps.libs.perferences.StringPreferenceType
import com.travlog.android.apps.libs.qualifiers.*
import com.travlog.android.apps.services.ApiClient
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.services.ApiService
import com.travlog.android.apps.services.interceptors.ApiRequestInterceptor
import com.travlog.android.apps.ui.SharedPreferenceKey
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun provideApiEndpoint(@ApiEndpointPreference apiEndpointPreference: StringPreferenceType): ApiEndpoint {
        return ApiEndpoint.from(apiEndpointPreference.get()!!)
    }

    @Provides
    @Singleton
    @ApiEndpointPreference
    internal fun provideApiEndpointPreference(sharedPreferences: SharedPreferences): StringPreferenceType {
        return StringPreference(sharedPreferences, "debug_api_endpoint", ApiEndpoint.STAGING.url())
    }

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return this.application
    }

    @Provides
    @Singleton
    @ApplicationContext
    internal fun provideApplicationContext(): Context {
        return this.application
    }

    @Provides
    @Singleton
    internal fun provideAssetManager(): AssetManager {
        return this.application.assets
    }

    @Provides
    @Singleton
    internal fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(this.application)
    }

    @Provides
    @Singleton
    internal fun provideEnvironment(apiClient: ApiClientType,
                                    build: Build,
                                    currentUser: CurrentUserType,
                                    sharedPreferences: SharedPreferences): Environment {

        return Environment(apiClient, build, currentUser, sharedPreferences)
    }

    @Provides
    @Singleton
    internal fun provideApiClientType(apiService: ApiService): ApiClientType {
        return ApiClient(apiService)
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(apiRequestInterceptor: ApiRequestInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()

        // Only log in debug mode to avoid leaking sensitive information.
        //        if (build.isDebug()) {
        builder.addInterceptor(httpLoggingInterceptor)
        //        }

        return builder
                .addInterceptor(apiRequestInterceptor)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideApiRetrofit(apiEndpoint: ApiEndpoint,
                                    okHttpClient: OkHttpClient): Retrofit {
        return createRetrofit(apiEndpoint.url(), okHttpClient)
    }

    @Provides
    @Singleton
    internal fun provideApiRequestInterceptor(currentUser: CurrentUserType,
                                              endpoint: ApiEndpoint): ApiRequestInterceptor {

        return ApiRequestInterceptor(currentUser, endpoint.url())
    }

    @Provides
    @Singleton
    internal fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    private fun createRetrofit(baseUrl: String,
                               okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                //                .client(SocketUtils.enableTLS1_2OnPreLollipop(okHttpClient))
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    @AccessTokenPreference
    internal fun provideAccessTokenPreference(sharedPreferences: SharedPreferences): StringPreferenceType {
        return StringPreference(sharedPreferences, SharedPreferenceKey.ACCESS_TOKEN)
    }

    @Provides
    @Singleton
    internal fun provideBuild(packageInfo: PackageInfo): Build {
        return Build(packageInfo)
    }

    @Provides
    @Singleton
    internal fun provideCurrentUser(@AccessTokenPreference accessTokenPreference: StringPreferenceType,
                                    @UserPreference userPreference: StringPreferenceType): CurrentUserType {

        return CurrentUser(accessTokenPreference, userPreference)
    }

    @Provides
    @Singleton
    internal fun providePackageInfo(application: Application): PackageInfo {
        try {
            return application.packageManager.getPackageInfo(application.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            throw RuntimeException()
        }

    }

    @Provides
    @Singleton
    @PackageNameString
    internal fun providePackageName(application: Application): String {
        return application.packageName
    }

    @Provides
    @Singleton
    internal fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    @UserPreference
    internal fun provideUserPreference(sharedPreferences: SharedPreferences): StringPreferenceType {
        return StringPreference(sharedPreferences, SharedPreferenceKey.USER)
    }
}
