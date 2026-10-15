package com.frodo.glamdring.domain.models;

import java.util.Objects;

/**
 * An already-hashed password. The domain never holds a raw password beyond
 * the moment it is handed to a {@link PasswordHasher}.
 */
public record PasswordHash(String value) {

    public PasswordHash {
        Objects.requireNonNull(value, "PasswordHash value must not be null");
        if (value.isBlank()) throw new IllegalArgumentException("PasswordHash value must not be blank");
    }

    @Override
    public String toString() {
        return "PasswordHash[****]";
    }
}
