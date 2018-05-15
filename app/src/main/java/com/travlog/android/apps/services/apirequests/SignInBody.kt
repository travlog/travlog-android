package com.travlog.android.apps.services.apirequests

class SignInBody {

    var userId = ""
    var loginId = ""
    var email = ""
    var password = ""
    var name = ""
    var profilePicture = ""
    var type = ""

    override fun toString(): String {
        return "SignInBody(userId='$userId', loginId='$loginId', email='$email', password='$password', name='$name', profilePicture='$profilePicture', type='$type')"
    }
}
