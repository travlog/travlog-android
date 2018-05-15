package com.travlog.android.apps.libs.db.realm

import android.content.Context

import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

class RealmHelper private constructor() {

    fun init(context: Context) {
        Realm.init(context)

        val configuration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(configuration)
    }

    fun deleteAllAsync() {
        Timber.d("deleteAllAsync: ")
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync(
                Realm.Transaction { it.deleteAll() },
                Realm.Transaction.OnSuccess { realm.close() })
    }

    companion object {

        private var instance: RealmHelper? = null

        fun getInstance(): RealmHelper {
            if (instance == null) {
                instance = RealmHelper()
            }
            return instance!!
        }
    }
}
