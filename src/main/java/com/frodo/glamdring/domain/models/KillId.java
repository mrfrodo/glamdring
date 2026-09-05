package com.frodo.glamdring.domain.models;

import java.util.Objects;

public record KillId(String value) {

    public KillId {
        Objects.requireNonNull(value, "KillId value must not be null");
        if (value.isBlank()) throw new IllegalArgumentException("KillId value must not be blank");
    }

    @Override
    public String toString() {
        return value;
    }
}
