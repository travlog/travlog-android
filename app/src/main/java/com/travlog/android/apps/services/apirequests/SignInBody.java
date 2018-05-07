package com.travlog.android.apps.services.apirequests;

public class SignInBody {

    public String userId;
    public String loginId;
    public String email;
    public String password;
    public String name;
    public String profilePicture;
    public String type;

    @Override
    public String toString() {
        return "XauthBody{" +
                "userId='" + userId + '\'' +
                ", loginId='" + loginId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
