package com.frodo.glamdring.domain.models;

import java.util.Objects;

public record TechId(String value) {

    public TechId {
        Objects.requireNonNull(value, "TechTrendId value must not be null");
        if (value.isBlank()) throw new IllegalArgumentException("TechTrendId value must not be blank");
    }

    @Override
    public String toString() {
        return value;
    }
}
