package com.frodo.glamdring.domain.models;

import java.util.Objects;

public record DiscussionId(String value) {

    public DiscussionId {
        Objects.requireNonNull(value, "DiscussionId value must not be null");
        if (value.isBlank()) throw new IllegalArgumentException("DiscussionId value must not be blank");
    }

    @Override
    public String toString() {
        return value;
    }
}
