package com.frodo.glamdring.domain.models;

import java.time.Instant;
import java.util.Objects;

/**
 * One remark spoken by one {@link Speaker} within a {@link Discussion}.
 */
public record DiscussionTurn(Speaker speaker, String message, Instant spokenAt) {

    public DiscussionTurn {
        Objects.requireNonNull(speaker, "speaker must not be null");
        Objects.requireNonNull(message, "message must not be null");
        if (message.isBlank()) throw new IllegalArgumentException("message must not be blank");
        Objects.requireNonNull(spokenAt, "spokenAt must not be null");
    }
}
