package com.travlog.android.apps.libs.db.realm

import android.content.Context
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Destination.Companion.FIELD_ID
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.models.Note.Companion.FIELD_CREATED_AT
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.Sort
import timber.log.Timber

object RealmHelper {

    fun init(context: Context) {
        Realm.init(context)

        val configuration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(configuration)
    }

    fun saveNoteAsync(note: Note) {
        Timber.d("saveNoteAsync: ${note.title}")

        Realm.getDefaultInstance().apply {
            executeTransaction { it.insertOrUpdate(note) }
                    .apply {
                        close()
                    }

//            executeTransactionAsync(
//                    Realm.Transaction { it.insertOrUpdate(note) },
//                    Realm.Transaction.OnSuccess { close() })
        }
    }

    fun getNoteAsync(realm: Realm, id: String): Note? {
        return realm.where(Note::class.java).equalTo(FIELD_ID, id).findFirst()
    }

    fun getAllNotesAsync(realm: Realm): RealmResults<Note> {
        Timber.d("getAllNotesAsync: ")

        return realm.where(Note::class.java).findAllAsync().sort(FIELD_CREATED_AT, Sort.DESCENDING)
    }

    fun saveDestinationAsync(destination: Destination) {
        Realm.getDefaultInstance().apply {
            executeTransaction { it.insertOrUpdate(destination) }.apply { close() }
        }
    }

    fun getDestinationAsync(realm: Realm, id: String): Destination {
        return realm.where(Destination::class.java).equalTo(FIELD_ID, id).findFirstAsync()
    }

    fun getAllDestinationsAsync(realm: Realm): RealmResults<Destination> {
        return realm.where(Destination::class.java).findAllAsync()
    }

    fun deleteAllAsync() {
        Timber.d("deleteAllAsync: ")
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync(
                Realm.Transaction { it.deleteAll() },
                Realm.Transaction.OnSuccess { realm.close() })
    }
}
