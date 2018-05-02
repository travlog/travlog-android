package com.travlog.android.apps.services.apiresponses;

import com.travlog.android.apps.models.User;

public class AccessTokenEnvelope extends Envelope {

    public Data data;

    public class Data {
        public String accessToken;
        public User user;
    }
}
