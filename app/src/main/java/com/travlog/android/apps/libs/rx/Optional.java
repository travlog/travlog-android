package com.travlog.android.apps.libs.rx;

import android.support.annotation.Nullable;

import java.util.NoSuchElementException;

public class Optional<M> {

    private final M optional;

    public Optional(@Nullable M optional) {
        this.optional = optional;
    }

    public boolean isNotEmpty() {
        return this.optional != null;
    }

    public boolean isEmpty() {
        return this.optional == null;
    }

    public M get() {
        if (optional == null) {
            throw new NoSuchElementException("No value present");
        }
        return optional;
    }
}
