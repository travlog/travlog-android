package com.travlog.android.apps.services.apiresponses;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.travlog.android.apps.services.ApiException;

public class Envelope {

    public String code;
    public int codeno;
    public boolean success;
    public Error err;

    public class Error {
        public String msg;
    }

    public static final int DUPLICATED_USER = 4009;

    /**
     * Tries to extract an {@link Envelope} from an exception, and if it
     * can't returns null.
     */
    public static @Nullable
    Envelope fromThrowable(final @NonNull Throwable t) {
        if (t instanceof ApiException) {
            final ApiException exception = (ApiException) t;
            return exception.errorEnvelope();
        }

        return null;
    }

    public boolean isDuplicatedUser() {
        return codeno == DUPLICATED_USER;
    }
}
