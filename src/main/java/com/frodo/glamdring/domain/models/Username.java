package com.frodo.glamdring.domain.models;

import java.util.Objects;

public record Username(String value) {

    private static final int MAX_LENGTH = 64;

    public Username {
        Objects.requireNonNull(value, "Username must not be null");
        value = value.strip();
        if (value.isEmpty()) throw new IllegalArgumentException("Username must not be blank");
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Username must be at most " + MAX_LENGTH + " characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
