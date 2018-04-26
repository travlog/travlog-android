package com.travlog.android.apps.libs.db.realm;

import android.content.Context;
import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class RealmHelper {

    private static RealmHelper instance;

    private RealmHelper() {
    }

    public static RealmHelper getInstance() {
        if (instance == null) {
            instance = new RealmHelper();
        }
        return instance;
    }

    public void init(final @NonNull Context context) {
        Realm.init(context);

        final RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    public void deleteAllAsync() {
        Timber.d("deleteAllAsync: ");
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(
                realm1 -> realm1.deleteAll(),
                realm::close);
    }
}
