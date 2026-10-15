package com.frodo.glamdring.domain.models;

/** Identity of a persisted user. Assigned by the database, so a new User has none until saved. */
public record UserId(int value) {

    public UserId {
        if (value <= 0) throw new IllegalArgumentException("UserId must be positive");
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
