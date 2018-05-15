package com.travlog.android.apps.services.apiresponses

import com.travlog.android.apps.models.User

data class AccessTokenEnvelope(var data: Data) : Envelope() {

    data class Data(var accessToken: String, var user: User)
}
