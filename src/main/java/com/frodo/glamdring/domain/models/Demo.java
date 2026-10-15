package com.frodo.glamdring.domain.models;

import java.util.Objects;

/**
 * A larger, runnable-looking walkthrough of a slay: a short summary of what
 * the reader will see, plus the Java source that shows it in action.
 */
public record Demo(String summary, String code) {

    public Demo {
        Objects.requireNonNull(summary, "summary must not be null");
        Objects.requireNonNull(code, "code must not be null");
        if (summary.isBlank()) throw new IllegalArgumentException("summary must not be blank");
        if (code.isBlank()) throw new IllegalArgumentException("code must not be blank");
    }
}
