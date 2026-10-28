package com.frodo.glamdring.domain.models.sd;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Identity of a system design: a URL-safe slug such as {@code large-file-upload}.
 * Chosen by the author, not generated, so the same design keeps the same identity
 * wherever it is stored.
 */
public record SystemDesignId(String value) {

    private static final Pattern SLUG = Pattern.compile("[a-z0-9]+(-[a-z0-9]+)*");

    public SystemDesignId {
        Objects.requireNonNull(value, "SystemDesignId must not be null");
        if (!SLUG.matcher(value).matches()) {
            throw new IllegalArgumentException("SystemDesignId must be a lowercase slug like 'large-file-upload', was '"
                    + value + "'");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
