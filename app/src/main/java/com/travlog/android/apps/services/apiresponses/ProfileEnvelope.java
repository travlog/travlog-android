package com.travlog.android.apps.services.apiresponses;

import com.travlog.android.apps.models.User;

public class ProfileEnvelope extends Envelope {

    public Data data;

    public class Data {
        public User user;
    }
}
