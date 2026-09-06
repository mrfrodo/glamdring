package com.frodo.glamdring.domain.models;

import java.util.Objects;

public record SlayId(String value) {

    public SlayId {
        Objects.requireNonNull(value, "SlayId value must not be null");
        if (value.isBlank()) throw new IllegalArgumentException("SlayId value must not be blank");
    }

    @Override
    public String toString() {
        return value;
    }
}
