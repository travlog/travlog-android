package com.travlog.android.apps.services.apirequests;

public class XauthBody {

    public String userId;
    public String email;
    public String password;
    public String name;
    public String type;

    @Override
    public String toString() {
        return "XauthBody{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
