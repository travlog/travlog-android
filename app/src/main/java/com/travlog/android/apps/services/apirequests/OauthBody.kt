package com.travlog.android.apps.services.apirequests

class OauthBody {

    var token = ""
    var provider = ""

    override fun toString(): String {
        return "OauthBody(token='$token', provider='$provider')"
    }
}
