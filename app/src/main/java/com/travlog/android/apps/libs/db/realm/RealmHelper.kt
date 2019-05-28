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

package com.travlog.android.apps.libs.db.realm

import android.content.Context
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Destination.Companion.FIELD_ID
import com.travlog.android.apps.models.Location
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

        RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
                .let {
                    Realm.setDefaultConfiguration(it)
                }
    }

    fun saveNoteAsync(note: Note) {
        Timber.d("saveNoteAsync: ${note.title}")

        Realm.getDefaultInstance()
                .apply {
                    executeTransaction {
                        note.destinations.forEachIndexed { index, destination ->
                            destination.order = index
                        }

                        it.insertOrUpdate(note)
                    }
                            .apply {
                                close()
                            }
                }
    }

    fun getNote(realm: Realm, id: String): Note? {
        Timber.d("getNote: $id")

        return realm.where(Note::class.java).equalTo(FIELD_ID, id).findFirst()
    }

    fun getAllNotes(realm: Realm): RealmResults<Note> {
        Timber.d("getAllNotes: ")

        return realm.where(Note::class.java).findAll().sort(FIELD_CREATED_AT, Sort.DESCENDING)
    }

    fun saveDestinationAsync(destination: Destination) {
        Timber.d("saveDestinationAsync: $destination")

        Realm.getDefaultInstance()
                .apply {
                    executeTransaction { it.insertOrUpdate(destination) }
                            .apply {
                                close()
                            }
                }
    }

    fun getDestination(realm: Realm, id: String): Destination? {
        Timber.d("getDestination: $id")

        return realm.where(Destination::class.java).equalTo(FIELD_ID, id).findFirst()
    }

    fun getAllDestinations(realm: Realm): RealmResults<Destination> {
        Timber.d("getAllDestinations: ")

        return realm.where(Destination::class.java).findAll()
    }

    fun getAllLocations(realm: Realm): RealmResults<Location> {
        Timber.d("getAllLocations: ")

        return realm.where(Location::class.java).findAll()
    }

    fun deleteAllAsync() {
        Timber.d("deleteAllAsync: ")

        Realm.getDefaultInstance()
                .apply {
                    executeTransactionAsync { it.deleteAll() }
                            .apply {
                                close()
                            }
                }
    }
}
