package com.frodo.glamdring.domain.models.sd;

/** Identity of a persisted system design. Assigned by the database. */
public record SystemDesignId(int value) {

    public SystemDesignId {
        if (value <= 0) throw new IllegalArgumentException("SystemDesignId must be positive");
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
