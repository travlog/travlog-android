package com.travlog.android.apps.services.apiresponses

import com.travlog.android.apps.models.User

data class ProfileEnvelope(var data: Data) : Envelope() {

    data class Data(var user: User)
}
