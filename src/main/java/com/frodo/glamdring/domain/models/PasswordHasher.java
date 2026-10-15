package com.frodo.glamdring.domain.models;

/**
 * Domain port for hashing and verifying passwords. Implemented in
 * infrastructure (e.g. by wrapping Spring's PasswordEncoder).
 */
public interface PasswordHasher {

    PasswordHash hash(String rawPassword);

    boolean matches(String rawPassword, PasswordHash hash);
}
