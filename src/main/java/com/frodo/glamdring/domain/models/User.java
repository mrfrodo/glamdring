package com.frodo.glamdring.domain.models;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregate root of the useradmin context. All changes to credentials, roles
 * and account state go through its behaviour so its invariants always hold:
 * a user always has at least one role, and a disabled user cannot authenticate.
 */
public final class User {

    private static final int MIN_PASSWORD_LENGTH = 12;

    private final UserId id; // null until the user has been persisted
    private final Username username;
    private PasswordHash passwordHash;
    private final Set<Role> roles;
    private boolean enabled;

    private User(UserId id, Username username, PasswordHash passwordHash, Set<Role> roles, boolean enabled) {
        this.id = id;
        this.username = Objects.requireNonNull(username, "username must not be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
        Objects.requireNonNull(roles, "roles must not be null");
        if (roles.isEmpty()) throw new IllegalArgumentException("A user must have at least one role");
        this.roles = EnumSet.copyOf(roles);
        this.enabled = enabled;
    }

    public static User register(Username username, String rawPassword, Role role, PasswordHasher hasher) {
        requireStrong(rawPassword);
        Objects.requireNonNull(role, "role must not be null");
        Objects.requireNonNull(hasher, "hasher must not be null");
        return new User(null, username, hasher.hash(rawPassword), EnumSet.of(role), true);
    }

    /** Rebuilds a persisted user; no validation of the raw password is possible or needed. */
    public static User reconstitute(UserId id, Username username, PasswordHash hash, Set<Role> roles, boolean enabled) {
        Objects.requireNonNull(id, "id must not be null");
        return new User(id, username, hash, roles, enabled);
    }

    public boolean isPersisted() {
        return id != null;
    }

    public boolean authenticate(String rawPassword, PasswordHasher hasher) {
        return enabled && rawPassword != null && hasher.matches(rawPassword, passwordHash);
    }

    public void changePassword(String currentRawPassword, String newRawPassword, PasswordHasher hasher) {
        if (!authenticate(currentRawPassword, hasher)) {
            throw new IllegalStateException("Current password is incorrect or account is disabled");
        }
        requireStrong(newRawPassword);
        this.passwordHash = hasher.hash(newRawPassword);
    }

    public void grant(Role role) {
        roles.add(Objects.requireNonNull(role, "role must not be null"));
    }

    public void revoke(Role role) {
        if (roles.contains(role) && roles.size() == 1) {
            throw new IllegalStateException("Cannot revoke the last role of a user");
        }
        roles.remove(role);
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public boolean isAdmin() {
        return hasRole(Role.ADMIN);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UserId id() {
        return id;
    }

    public Username username() {
        return username;
    }

    public PasswordHash passwordHash() {
        return passwordHash;
    }

    public Set<Role> roles() {
        return Set.copyOf(roles);
    }

    private static void requireStrong(String rawPassword) {
        Objects.requireNonNull(rawPassword, "password must not be null");
        if (rawPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User other && username.equals(other.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
