package com.travlog.android.apps.viewmodels.outputs

import io.reactivex.Completable

interface PostNoteViewModelOutputs {

    fun setResultAndBack(): Completable
}
