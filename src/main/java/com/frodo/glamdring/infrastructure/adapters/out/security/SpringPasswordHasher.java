package com.frodo.glamdring.infrastructure.adapters.out.security;

import com.frodo.glamdring.domain.models.PasswordHash;
import com.frodo.glamdring.domain.models.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Adapter: the domain's {@link PasswordHasher} port backed by Spring's PasswordEncoder. */
@Component
public class SpringPasswordHasher implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    public SpringPasswordHasher(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PasswordHash hash(String rawPassword) {
        return new PasswordHash(passwordEncoder.encode(rawPassword));
    }

    @Override
    public boolean matches(String rawPassword, PasswordHash hash) {
        return passwordEncoder.matches(rawPassword, hash.value());
    }
}
