package com.travlog.android.apps.services.apiresponses

import com.travlog.android.apps.models.Account

data class AccountsEnvelope(var data: Data) : Envelope() {

    data class Data(var accounts: List<Account>)
}
