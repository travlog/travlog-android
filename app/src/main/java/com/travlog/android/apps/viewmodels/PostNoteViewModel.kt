package com.travlog.android.apps.viewmodels

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import com.travlog.android.apps.libs.ActivityRequestCodes.DESTINATION
import com.travlog.android.apps.libs.ActivityViewModel
import com.travlog.android.apps.libs.Environment
import com.travlog.android.apps.libs.db.realm.RealmHelper
import com.travlog.android.apps.libs.rx.Optional
import com.travlog.android.apps.libs.rx.bus.NoteEvent
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverApiError
import com.travlog.android.apps.libs.rx.transformers.Transformers.neverError
import com.travlog.android.apps.libs.rx.transformers.Transformers.takeWhen
import com.travlog.android.apps.models.Destination
import com.travlog.android.apps.models.Note
import com.travlog.android.apps.services.ApiClientType
import com.travlog.android.apps.ui.IntentKey
import com.travlog.android.apps.ui.activities.PostNoteActivity
import com.travlog.android.apps.ui.adapters.DestinationAdapter
import com.travlog.android.apps.ui.viewholders.DestinationViewHolder
import com.travlog.android.apps.viewmodels.errors.PostNoteViewModelErrors
import com.travlog.android.apps.viewmodels.inputs.PostNoteViewModelInputs
import com.travlog.android.apps.viewmodels.outputs.PostNoteViewModelOutputs
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class PostNoteViewModel @Inject constructor(environment: Environment
) : ActivityViewModel<PostNoteActivity>(environment),
        PostNoteViewModelInputs, PostNoteViewModelOutputs, PostNoteViewModelErrors, DestinationAdapter.Delegate {

    private val apiClient: ApiClientType = environment.apiClient

    private val title = PublishSubject.create<String>()
    private val saveClick = PublishSubject.create<Optional<*>>()

    private val setSaveButtonEnabled = BehaviorSubject.create<Boolean>()
    private val addDestination = BehaviorSubject.create<Destination>()
    private val setResultAndBack = CompletableSubject.create()

    val inputs: PostNoteViewModelInputs = this
    val outputs: PostNoteViewModelOutputs = this
    val errors: PostNoteViewModelErrors = this

    init {
        val note = Note()

        val destinationObservable = activityResult()
                .filter { it.requestCode == DESTINATION }
                .filter { it.resultCode == RESULT_OK }
                .map { it.intent?.getStringExtra(IntentKey.DESTINATION_ID) ?: "" }
                .switchMap {
                    RealmHelper.getDestination(realm, it)
                            ?.asFlowable<Destination>()
                            ?.firstElement()
                            ?.toObservable()
                }

        destinationObservable
                .compose(bindToLifecycle())
                .subscribe(addDestination)

        title.map(this::isValid)
                .compose(bindToLifecycle())
                .subscribe(setSaveButtonEnabled)

        Observable.merge(
                title.map {
                    note.title = it
                    note
                },
                destinationObservable.map {
                    Timber.d("destination? $it, location? ${it.location}")
                    note.destinations.add(it)
                    note
                })
                .compose<Note>(takeWhen(saveClick))
                .doOnNext { it.id = RealmHelper.getAllNotes(realm).size.toString() }
                .doOnNext { RealmHelper.saveNoteAsync(it) }
//                .switchMap {
//                    this.post(it)
//                            .doOnSubscribe {
//
//                            }
//                            .doAfterTerminate {
//
//                            }
//                }
                .compose(bindToLifecycle())
                .subscribe { postSuccess(it) }
    }

    private fun isValid(title: String): Boolean {
        return title.isNotBlank()
    }

    private fun postSuccess(note: Note) {
        NoteEvent.getInstance().post(note)
        setResultAndBack.onComplete()
    }

    private fun post(note: Note): Observable<Note> =
            apiClient.postNote(note)
                    .compose(neverApiError())
                    .compose(neverError())
                    .toObservable()

    override fun title(title: String) = this.title.onNext(title)

    override fun saveClick() = this.saveClick.onNext(Optional<Any>(null))

    override fun setSaveButtonEnabled(): Observable<Boolean> = setSaveButtonEnabled

    override fun addDestination(): Observable<Destination> = addDestination

    override fun setResultAndBack(): Completable = setResultAndBack

    override fun destinationViewHolderItemClick(viewHolder: DestinationViewHolder, destination: Destination) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
