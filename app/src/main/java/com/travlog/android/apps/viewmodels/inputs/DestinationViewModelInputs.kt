package com.travlog.android.apps.viewmodels.inputs

import java.util.*

interface DestinationViewModelInputs {

    fun location(location: String)

    fun startDate(date: Date?)

    fun endDate(date: Date?)

    fun saveClick()
}
