package com.travlog.android.apps.services.apiresponses;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.travlog.android.apps.services.ApiException;

public class ErrorEnvelope {

    public String errorMessage;
    public int httpCode;
    public String tfcCode;

    public static final String MISSING_FACEBOOK_EMAIL = "missing_facebook_email";
    public static final String FACEBOOK_INVALID_ACCESS_TOKEN = "facebook_invalid_access_token";
    public static final String UNAUTHORIZED = "unauthorized";

    /**
     * Tries to extract an {@link ErrorEnvelope} from an exception, and if it
     * can't returns null.
     */
    public static @Nullable
    ErrorEnvelope fromThrowable(final @NonNull Throwable t) {
        if (t instanceof ApiException) {
            final ApiException exception = (ApiException) t;
            return exception.errorEnvelope();
        }

        return null;
    }


    public boolean isMissingFacebookEmailError() {
        return MISSING_FACEBOOK_EMAIL.equals(tfcCode);
    }

    public boolean isFacebookInvalidAccessTokenError() {
        return FACEBOOK_INVALID_ACCESS_TOKEN.equals(tfcCode);
    }

    public boolean isUnauthorizedError() {
        return UNAUTHORIZED.equals(tfcCode);
    }
}
