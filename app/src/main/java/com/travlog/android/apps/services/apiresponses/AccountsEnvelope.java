package com.travlog.android.apps.services.apiresponses;

import com.travlog.android.apps.models.Account;

import java.util.List;

public class AccountsEnvelope extends Envelope {

    public Data data;

    public class Data {
        public List<Account> accounts;
    }
}
